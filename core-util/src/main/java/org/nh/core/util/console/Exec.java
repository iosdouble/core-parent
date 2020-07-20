package org.nh.core.util.console;

import com.alibaba.dubbo.common.URL;
import com.alibaba.fastjson.JSONObject;
import com.gome.gscm.console.config.common.Cfg;
import com.gome.gscm.console.config.git.BaselineGitConfig;
import com.gome.gscm.console.config.git.BranchGitConfig;
import com.gome.gscm.console.config.svn.BaselineConfig;
import com.gome.gscm.console.config.svn.BranchConfig;
import com.gome.gscm.context.Context;
import com.gome.gscm.diamond.Diamond;
import com.gome.gscm.diamond.DiamondException;
import com.gome.gscm.generate.GenerateBase;
import com.gome.gscm.svn.Secure;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.Session;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import redis.GcacheClient;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

class Shell {
    private ChannelShell openChannel;

    public void setOpenChannel(ChannelShell openChannel) {
        this.openChannel = openChannel;
    }

    public ChannelShell getOpenChannel() {
        return openChannel;
    }
}

class RankBean {

    private String ip;
    private double percent;

    public RankBean(String ip, double percent) {
        this.ip = ip;
        this.percent = percent;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "[" +
                "'" + ip + '\'' +
                ":" + percent +
                ']';
    }
}

class CpuPickRule extends SortableExecRule {
    @Override
    public RuleResult pick(Collection<MachineStatus> machineStatusList) {
        if (machineStatusList == null || machineStatusList.isEmpty())
            return null;
        List<RankBean> sortCpu = new ArrayList<>();
        Data<Boolean> isSame = new Data<>();
        if(!sort(machineStatusList, new IData() {
            @Override
            public double getData(MachineStatus m) {
                return m.getCpuUserUsagePercent();
            }
            @Override
            public double getDelta() {
                return 5.0;
            }
            @Override
            public double getMax() {
                return 99.0;
            }
            @Override
            public String getDiamondKey() {
                return "gscm.script.level.cpu";
            }
        }, isSame, sortCpu))
            return null;
        return new RuleResult(sortCpu.get(0).getIp(), isSame.getData(), "CPU: " + Arrays.toString(sortCpu.toArray()));
    }
}

class MemoryPickRule extends SortableExecRule {
    @Override
    public RuleResult pick(Collection<MachineStatus> machineStatusList) {

        if (machineStatusList == null || machineStatusList.isEmpty())
            return null;

        List<RankBean> sortMemory = new ArrayList<>();
        Data<Boolean> isSame = new Data<>();
        if(!sort(machineStatusList, new IData() {
            @Override
            public double getData(MachineStatus m) {
                return m.getMemoryUsagePercent()*100.0;
            }
            @Override
            public double getDelta() {
                return 20.0;
            }
            @Override
            public double getMax() {
                return 90.0;
            }
            @Override
            public String getDiamondKey() {
                return "gscm.script.level.mem";
            }
        }, isSame, sortMemory))
            return null;
        return new RuleResult(sortMemory.get(0).getIp(), isSame.getData(), "Mem: "+Arrays.toString(sortMemory.toArray()));
    }
}


class DiskPickRule implements IExecRule{

    private static final Logger logger = Logger.getLogger(DiskPickRule.class);

    @Override
    public RuleResult pick(Collection<MachineStatus> machineStatusList) {
        if (machineStatusList == null || machineStatusList.isEmpty())
            return null;

        String disk = null;
        try {
            disk = Diamond.getCurrDisk();
        } catch (DiamondException e) {
            logger.error(e.getMessage(), e);
        }
        if(disk == null)
            return null;
        List<RankBean> sortDisk = new ArrayList<>();
        for (MachineStatus m : machineStatusList) {
            sortDisk.add(new RankBean(m.getIp(), BigDecimal.valueOf(m.getDiskUsagePercent().get(disk)*100.0).setScale(2, RoundingMode.UP).doubleValue()));
        }

        Collections.sort(sortDisk, new Comparator<RankBean>() {
            @Override
            public int compare(RankBean o1, RankBean o2) {
                return (int) (o1.getPercent() - o2.getPercent());
            }
        });

        return new RuleResult(sortDisk.get(0).getIp(), false, "Disk["+disk+"]: "+Arrays.toString(sortDisk.toArray()));

    }
}

interface CompileRunnable extends Runnable {
    Long getTagsId();
}


class NamedThreadFactory implements ThreadFactory {

    private final AtomicInteger mThreadNum = new AtomicInteger(1);

    private final String mPrefix;

    private final boolean mDaemo;

    private final ThreadGroup mGroup;

    public NamedThreadFactory(String prefix, boolean daemo) {
        mPrefix = prefix + "-thread-";
        mDaemo = daemo;
        SecurityManager s = System.getSecurityManager();
        mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        String name = mPrefix + mThreadNum.getAndIncrement();
        Thread ret = new Thread(mGroup, runnable, name, 0);
        ret.setDaemon(mDaemo);
        return ret;
    }

    public ThreadGroup getThreadGroup() {
        return mGroup;
    }
}

public class Exec extends GenerateBase implements InitializingBean, DisposableBean, ConsoleApi, PathChildrenCacheListener, ApplicationContextAware {
    private static final Logger logger = Logger.getLogger(Exec.class);
    private static GcacheClient cacheService;
    private static final ArrayBlockingQueue<Map.Entry<String, String>> LAST_SUCCESS_SERVER = new ArrayBlockingQueue<>(1);
    private static final Map<String, MachineStatus> machineStatusMap = new ConcurrentHashMap<>();
    private static final Map<String, GenericObjectPool<Session>> SESSION = new ConcurrentHashMap<>();

    private static final Map<Long, CompileWork> work = new ConcurrentHashMap<>();

    //Git System
    private static final Map<Long, CompileWork> gitWork = new ConcurrentHashMap<>();

    private static final List<IExecRule> pickRules = new ArrayList<>();

    private static Date lastRecordDate = null;
    private static final Schema<JSONObject> schema = RuntimeSchema
            .getSchema(JSONObject.class);

    private static CuratorFramework zookeeper;
    static {
        RetryPolicy rp = new ExponentialBackoffRetry(1000, 3);//重试机制
        CuratorFrameworkFactory.Builder builder;
        try {
            builder = CuratorFrameworkFactory.builder().connectString(Diamond.getString("dubbo.addr.gome"))
                    .connectionTimeoutMs(5000)
                    .sessionTimeoutMs(5000)
                    .retryPolicy(rp);
            zookeeper = builder.build();
        } catch (DiamondException e) {
            logger.error(e.getMessage(), e);
        }
    }
    private static CountDownLatch zkLatch = new CountDownLatch(1);

    private static final Map<String, AtomicLong> peopleAmount = new ConcurrentHashMap<>();
    private static final Map<String, AtomicLong> callAmount = new ConcurrentHashMap<>();

    private static final ArrayBlockingQueue<RunWork> runWork = new ArrayBlockingQueue<>(30);

    private static final ReentrantLock pickLock = new ReentrantLock();
    private static final ReentrantLock recordLock = new ReentrantLock();

    private static final ExecutorService _EXE = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() + 1, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),new NamedThreadFactory("GSCM", true));
    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() + 1, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),new NamedThreadFactory("GSCM-编译线程", true));
//    private static final ExecutorService _EXE = Executors.newCachedThreadPool(new NamedThreadFactory("GSCM", true));
//    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(new NamedThreadFactory("GSCM-编译线程", true));
    private static volatile String _usableIp = "";
    private static volatile String _rank = "";

    private static volatile String _status = "";

    /**
     * 编译脚本
     */
    public static final String SVN_COMPILE = "auto_build_new.sh";
    public static final String GIT_COMPILE = "auto_build_git.sh";

    /**
     * 创建模块化目录
     */
    public static final String SVN_CREATE_MODULE_PATH = "create_new_module.sh";

    /**
     * 创建非模块化目录
     */
    public static final String SVN_CREATE_NO_MODULE_PATH = "create_new_no_module.sh";

    /**
     * 创建分支
     */
    public static final String SVN_CREATE_BRANCH = "create_new_branch.sh";
    public static final String GIT_CREATE_BRANCH = "create_new_branch_git.sh";

    /**
     * 发布基线
     */
    public static final String SVN_PUBLISH_BASELINE = "auto_release.sh";
    public static final String GIT_PUBLISH_BASELINE = "auto_release_git.sh";


    public static Map<String, GenericObjectPool<Session>> getSession(){
        return SESSION;
    }

    public static ArrayBlockingQueue<RunWork> getRunWork(){
        return runWork;
    }

    public static Map<String, AtomicLong> getPeopleAmount(){
        return peopleAmount;
    }

    public static Map<String, AtomicLong> getCallAmount(){
        return callAmount;
    }

    public static void setUsableIp(String ip){
        _usableIp = ip;
    }

    public static String getStatus(){
        return _status;
    }

    private static void initSpring(ApplicationContext context){
        cacheService = (GcacheClient)context.getBean("cacheService");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        initSpring(applicationContext);
    }


    static class RunLogThread implements Runnable{
        private Sys sys;
        private String ipAddr;
        private String userName;
        private String commandStr;
        private GcacheClient client;

        RunLogThread(Sys sys, String ipAddr, String userName, String commandStr, GcacheClient client){
            this.sys = sys;
            this.ipAddr = ipAddr;
            this.userName = userName;
            this.commandStr = commandStr;
            this.client = client;
        }

        private static void updateLastRecordDate(Calendar c){
            lastRecordDate = c.getTime();
        }

        @Override
        public void run() {
            try {
                String ip = Diamond.isInCompileMachineTestMode()? Diamond.getString("compile.machine.test.ip") : _usableIp;
                String rank = _rank;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if(runWork.size() == 30) {
                    runWork.poll();
                }
                if(callAmount.get(ip) != null){
                    callAmount.get(ip).incrementAndGet();
                }
                if(peopleAmount.get(userName) == null){
                    peopleAmount.put(userName, new AtomicLong(1L));
                }else
                    peopleAmount.get(userName).incrementAndGet();
                runWork.offer(new RunWork(sys, ipAddr, userName, df.format(new Date()), ip, commandStr, rank));
                recordLock.lock();
                Calendar c = Calendar.getInstance();
                if(client != null && (lastRecordDate == null || ( c.getTime().getTime() - lastRecordDate.getTime() ) > 2 * 60 * 1000L)){
                    updateLastRecordDate(c);
                    JSONObject callAmountMap = new JSONObject();
                    byte[] val;
                    if((val = client.getBytes("GSCM_CALL_AMOUNT_CACHE_")) != null){
                        ProtostuffIOUtil.mergeFrom(val, callAmountMap, schema);
                    }
                    JSONObject peopleAmountMap = new JSONObject();
                    if((val = client.getBytes("GSCM_PEOPLE_AMOUNT_CACHE_")) != null){
                        ProtostuffIOUtil.mergeFrom(val, peopleAmountMap, schema);
                    }
                    for(Map.Entry<String, AtomicLong> entry : callAmount.entrySet())
                        callAmountMap.put(entry.getKey(), entry.getValue().get());

                    for(Map.Entry<String, AtomicLong> entry : peopleAmount.entrySet())
                        peopleAmountMap.put(entry.getKey(), entry.getValue().get());

                    LinkedBuffer buffer = LinkedBuffer.allocate(1024);
                    byte[] protoStuff = ProtostuffIOUtil.toByteArray(callAmountMap, schema, buffer);
                    client.set("GSCM_CALL_AMOUNT_CACHE_", protoStuff);
                    buffer.clear();
                    protoStuff = ProtostuffIOUtil.toByteArray(peopleAmountMap, schema, buffer);
                    client.set("GSCM_PEOPLE_AMOUNT_CACHE_", protoStuff);
                    buffer.clear();
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                recordLock.unlock();
            }

        }
    }

    private static void recordRunLog(final Sys sys, final String ipAddr, final String userName, final String commandStr){
        if(!Context.isTestEnvironment())
            _EXE.execute(new RunLogThread(sys, ipAddr, userName, commandStr, cacheService));
    }

    /**
     * SVN 项目编译
     */
    static class SvnCompileRunnable implements CompileRunnable{

        private String userName;
        private String ipAddr;
        private ICompileHandler compileHandler;
        private Long tagsId;
        private String repoName;
        private String moduleName;
        private String moduleUrl;
        private String branchVersion;
        private String tagVersion;

        private String jdk;

        public SvnCompileRunnable(String userName, String ipAddr, ICompileHandler compileHandler, Long tagsId,
                                  String repoName, String moduleName, String moduleUrl, String branchVersion,
                                  String tagVersion) {
            this.userName = userName;
            this.ipAddr = ipAddr;
            this.compileHandler = compileHandler;
            this.tagsId = tagsId;
            this.repoName = repoName;
            this.moduleName = moduleName;
            this.moduleUrl = moduleUrl;
            this.branchVersion = branchVersion;
            this.tagVersion = tagVersion;
        }

        public SvnCompileRunnable(String userName, String ipAddr, ICompileHandler compileHandler, Long tagsId,
                                  String repoName, String moduleName, String moduleUrl, String branchVersion,
                                  String tagVersion,String jdk) {
            this.userName = userName;
            this.ipAddr = ipAddr;
            this.compileHandler = compileHandler;
            this.tagsId = tagsId;
            this.repoName = repoName;
            this.moduleName = moduleName;
            this.moduleUrl = moduleUrl;
            this.branchVersion = branchVersion;
            this.tagVersion = tagVersion;
            this.jdk = jdk;
        }


        @Override
        public Long getTagsId() {
            return tagsId;
        }

        @Override
        public void run() {
            ConsoleStatus status = new ConsoleStatus();
            try {
                work.put(tagsId, new CompileWork(tagsId));
                final String fullUrl = "/" + repoName + moduleUrl;
                String branchName = branchName(moduleName, branchVersion);
                StringBuilder command = commandBuilder(Sys.SVN, SVN_COMPILE);
                logger.info("编译队列大小 "+work.size());
                command.append(" ").append(fullUrl);
                command.append(" ").append(branchName);
                command.append(" ").append(tagVersion);
                command.append(" ").append(jdk);
                System.out.println("输出编译命令"+command.toString());
                recordRunLog(Sys.SVN, ipAddr, userName, command.toString());
                status = executeInMachine(Sys.SVN, tagsId, command.toString(), new Cfg() {
                    @Override
                    public String getPath() {
                        return fullUrl.substring(1);
                    }
                });
                status.setOutput(work.get(tagsId).getCompileLog().toString());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                status.setOutput(e.getMessage());
            } finally {
                work.remove(tagsId);
                if (compileHandler != null)
                    compileHandler.onCompileComplete(tagsId, repoName, moduleName, moduleUrl, branchVersion, tagVersion, status);
            }
        }
    }

    /**
     * GIT 项目编译
     */
    static class GitCompileRunnable implements CompileRunnable{

        private String userName;
        private String ipAddr;
        private IGitCompileHandler gitCompileHandler;
        private Long gitTagsId;
        private String projectName;
        private String gitPath;
        private String branchVersion;
        private String tagVersion;

        private String jdk;

        public GitCompileRunnable(String userName, String ipAddr, IGitCompileHandler gitCompileHandler,
                                  Long gitTagsId, String projectName, String gitPath, String branchVersion,
                                  String tagVersion) {
            this.userName = userName;
            this.ipAddr = ipAddr;
            this.gitCompileHandler = gitCompileHandler;
            this.gitTagsId = gitTagsId;
            this.projectName = projectName;
            this.gitPath = gitPath;
            this.branchVersion = branchVersion;
            this.tagVersion = tagVersion;

        }

        public GitCompileRunnable(String userName, String ipAddr, IGitCompileHandler gitCompileHandler,
                                  Long gitTagsId, String projectName, String gitPath, String branchVersion,
                                  String tagVersion,String jdk) {
            this.userName = userName;
            this.ipAddr = ipAddr;
            this.gitCompileHandler = gitCompileHandler;
            this.gitTagsId = gitTagsId;
            this.projectName = projectName;
            this.gitPath = gitPath;
            this.branchVersion = branchVersion;
            this.tagVersion = tagVersion;
            this.jdk = jdk;
        }
        @Override
        public Long getTagsId() {
            return gitTagsId;
        }

        @Override
        public void run() {
            ConsoleStatus status = new ConsoleStatus();
            try {
                gitWork.put(gitTagsId, new CompileWork(gitTagsId));
                String branchName = branchName(projectName, branchVersion);
                logger.info("编译队列 大小 "+ gitWork.size());

                //Git Compile Command
                StringBuilder command = commandBuilder(Sys.GIT, GIT_COMPILE);
                command.append(" ").append(gitPath);
                command.append(" ").append(branchName);
                command.append(" ").append(tagVersion);
                command.append(" ").append(jdk);
                recordRunLog(Sys.GIT, ipAddr, userName, command.toString());
                status = executeInMachine(Sys.GIT, gitTagsId, command.toString(), new Cfg() {
                    @Override
                    public String getPath() {
                        return gitPath;
                    }
                });
                status.setOutput(gitWork.get(gitTagsId).getCompileLog().toString());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                status.setOutput(e.getMessage());
            } finally {
                gitWork.remove(gitTagsId);
                if (gitCompileHandler != null)
                    gitCompileHandler.onCompileComplete(gitTagsId, projectName, gitPath, branchVersion, tagVersion, status);
            }
        }
    }


    private static StringBuilder commandBuilder(Sys sys, String script) throws ScriptException {
        StringBuilder command = null;
        try {
            command = new StringBuilder(Diamond.getString("gscm.script.path") + "/" + script);
            if (Diamond.getInteger("gscm.script.enable.full") == 1) {
                switch (sys){
                    case SVN:
                        command.append(" '").append(Diamond.getString("gscm.svn.base.url")).append('\'');
                        command.append(" '").append(Diamond.getString("gscm.module.path")).append('\'');
                        command.append(" '").append(Diamond.getString("gscm.script.check.url")).append('\'');
                        break;
                    case GIT:
                        break;
                    default:
                        break;
                }

            }
        } catch (DiamondException e) {
            logger.error(e.getMessage(), e);
        }
        if(command == null){
            throw new ScriptException("脚本执行中配置文件读取异常");
        }
        return command;
    }

    /**
     * Git编译
     * @param gitCompileHandler
     * @param gitTagsId
     * @param projectName
     * @param gitPath
     * @param branchVersion
     * @param tagVersion
     */
    public static void compile(final IGitCompileHandler gitCompileHandler,final Long gitTagsId,final String projectName,
                               final String gitPath,final String branchVersion,final String tagVersion){
        EXECUTOR.execute(new GitCompileRunnable(Context.getUser(), Context.getIp(), gitCompileHandler, gitTagsId,
                projectName, gitPath, branchVersion, tagVersion));
    }


    /**
     * Git编译 JDK
     * @param gitCompileHandler
     * @param gitTagsId
     * @param projectName
     * @param gitPath
     * @param branchVersion
     * @param tagVersion
     */
    public static void compileByJDK(final IGitCompileHandler gitCompileHandler,final Long gitTagsId,final String projectName,
                               final String gitPath,final String branchVersion,final String tagVersion,final String jdk){
        EXECUTOR.execute(new GitCompileRunnable(Context.getUser(), Context.getIp(), gitCompileHandler, gitTagsId,
                projectName, gitPath, branchVersion, tagVersion,jdk));
    }

    /**
     * 编译
     *
     * @param repoName      仓库名称
     * @param moduleName    　   模块名称
     * @param moduleUrl     　　  模块路径
     * @param branchVersion 所在分支版本
     * @param tagVersion    　　　tag版本
     * @throws Exception
     */
    public static void compile(final ICompileHandler compileHandler, final Long tagsId, final String repoName,
                               final String moduleName, final String moduleUrl, final String branchVersion,
                               final String tagVersion) {
        EXECUTOR.execute(new SvnCompileRunnable(Context.getUser(), Context.getIp(), compileHandler, tagsId, repoName,
                moduleName, moduleUrl, branchVersion, tagVersion));

    }

    /**
     * SVN JDK 编译
     * @param compileHandler
     * @param tagsId
     * @param repoName
     * @param moduleName
     * @param moduleUrl
     * @param branchVersion
     * @param tagVersion
     */
    public static void compileByJDK(final ICompileHandler compileHandler, final Long tagsId, final String repoName,
                               final String moduleName, final String moduleUrl, final String branchVersion,
                               final String tagVersion,final String jdk) {
        EXECUTOR.execute(new SvnCompileRunnable(Context.getUser(), Context.getIp(), compileHandler, tagsId, repoName,
                moduleName, moduleUrl, branchVersion, tagVersion,jdk));

    }

    /**
     * 脚本创建模块化目录
     *
     * @param repoName   仓库名称              pp_b
     * @param moduleUrl  模块路径              /gome-gcc/gome-gcc-cms
     * @throws Exception
     */
    public static void createModulePath(String repoName, String moduleUrl) throws ScriptException {
        createPath(SVN_CREATE_MODULE_PATH, repoName, moduleUrl);
    }

    /**
     * 脚本创建非模块化目录
     *
     * @param repoName     仓库名称                  pp_b
     * @param documentUrl  非模块路径              /产品文档/项目进度
     * @throws Exception
     */
    public static void createNoModulePath(String repoName, String documentUrl) throws ScriptException {
        createPath(SVN_CREATE_NO_MODULE_PATH, repoName, documentUrl);
    }


    private static void createPath(String script, String repoName, String url) throws ScriptException {
        final String userName = Context.getUser();
        final String ipAddr = Context.getIp();

        final String fullUrl = "/" + repoName + url;

        StringBuilder command = commandBuilder(Sys.SVN, script);
        command.append(" ").append(fullUrl);

        recordRunLog(Sys.SVN, ipAddr, userName, command.toString());

        ConsoleStatus status = executeInMachine(Sys.SVN, null, command.toString(), new Cfg() {
            @Override
            public String getPath() {
                return fullUrl.substring(1);
            }
        });

        if (!status.isSuccess())
            throw new ScriptException("远程执行异常: " + status.getOutput());

        switch (status.getStatus()) {
            case 0:
                return;
            default:
                throw new ScriptException("错误: " + status.getOutput());
        }
    }


    /**
     * 调用脚本创建GIT分支
     *
     * @return
     */
    public static void createBranch(BranchGitConfig config) throws ScriptException {
        final String userName = Context.getUser();
        final String ipAddr = Context.getIp();

        String branchName = branchName(config.getProjectName(), config.getBranchVersion());
        String baselineName = baselineName(config.getProjectName(), config.getBaselineVersion());

        StringBuilder command = commandBuilder(Sys.GIT, GIT_CREATE_BRANCH);
        command.append(" ").append(config.getGitPath());
        command.append(" ").append(branchName);
        command.append(" ").append(baselineName);

        recordRunLog(Sys.SVN, ipAddr, userName, command.toString());

        ConsoleStatus status = executeInMachine(Sys.GIT, null, command.toString(), config);

        if (!status.isSuccess())
            throw new ScriptException("远程执行异常: " + status.getOutput());

        switch (status.getStatus()) {
            case 0:
                return;

            default:
                throw new ScriptException("创建Git分支发生错误: " + status.getOutput());
        }
    }

    /**
     * 调用脚本创建分支
     *
     * @return
     */
    public static void createBranch(BranchConfig config) throws ScriptException {
        final String userName = Context.getUser();
        final String ipAddr = Context.getIp();

        String fullUrl = "/" + config.getRepoName() + config.getModuleUrl();
        String branchName = branchName(config.getModuleName(), config.getBranchVersion());
        String baselineName = baselineName(config.getModuleName(), config.getBaselineVersion());

        StringBuilder command = commandBuilder(Sys.SVN, SVN_CREATE_BRANCH);
        command.append(" ").append(fullUrl);
        command.append(" ").append(branchName);
        command.append(" ").append(baselineName);

        recordRunLog(Sys.SVN, ipAddr, userName, command.toString());

        ConsoleStatus status = executeInMachine(Sys.SVN, null, command.toString(), config);

        if (!status.isSuccess())
            throw new ScriptException("远程执行异常: " + status.getOutput());

        switch (status.getStatus()) {
            case 0:
                return;

            case 1:
                throw new ScriptException("模块 [ " + config.getModuleName() + " at " + config.getModuleUrl() + " ] 不是模块化目录的叶子节点");

            case 2:
                throw new ScriptException("路径 [ " + config.getModuleUrl() + " ]校验发生错误");

            case 3:
                throw new ScriptException("基线 [ " + baselineName + " ] 不存在");

            case 4:
                throw new ScriptException("分支创建失败: " + status.getOutput());

            default:
                throw new ScriptException("创建分支发生错误: " + status.getOutput());
        }
    }

    /**
     * 调用脚本发布Git基线
     * @throws ScriptException
     */
    public static void createBaseline(BaselineGitConfig config) throws ScriptException {

        final String userName = Context.getUser();
        final String ipAddr = Context.getIp();

        String tagName = tagsName(config.getProjectName(), config.getTagVersion());
        String baselineOfBranchName = baselineName(config.getProjectName(), config.getBaselineOfBranchVersion());
        String newestBaselineName = baselineName(config.getProjectName(), config.getNewestBaselineVersion());
        String branchName = branchName(config.getProjectName(), config.getBranchVersion());


        StringBuilder command = commandBuilder(Sys.GIT, GIT_PUBLISH_BASELINE);
        command.append(" ").append(config.getGitPath());
        command.append(" ").append(tagName);
        command.append(" ").append(baselineOfBranchName);
        command.append(" ").append(newestBaselineName);
        command.append(" ").append(branchName);
        command.append(" ").append(config.getNewStableVersion());

        recordRunLog(Sys.GIT, ipAddr, userName, command.toString());

        ConsoleStatus status = executeInMachine(Sys.GIT, null, command.toString(), config);


        if (!status.isSuccess())
            throw new ScriptException("远程执行异常: " + status.getOutput());

        switch (status.getStatus()) {
            case 0:
                return;

            case 5:
                throw new ScriptException("如果你开发的分支所在的基线已经不是最新基线，发布时请先合并最新基线代码到你的分支，" +
                        "并手动打一tag,tag的命名规则为:模块名称_最新基线的四位版本_MergeTo_分支版本.例如：" +
                        config.getProjectName() + "_" + config.getNewestBaselineVersion() + "_MergeTo_" + config.getBranchVersion());
            case 9:
                throw new ScriptException("合并代码后，请重新创建四位版本编译测试通过后再发布");


            default:
                throw new ScriptException("创建Git基线发生错误: " + status.getOutput());
        }
    }


    /**
     * 调用脚本发布Git基线
     * @throws ScriptException
     */
    public static void createBaselineWithJDK(BaselineGitConfig config,String jdk) throws ScriptException {

        final String userName = Context.getUser();
        final String ipAddr = Context.getIp();

        String tagName = tagsName(config.getProjectName(), config.getTagVersion());
        String baselineOfBranchName = baselineName(config.getProjectName(), config.getBaselineOfBranchVersion());
        String newestBaselineName = baselineName(config.getProjectName(), config.getNewestBaselineVersion());
        String branchName = branchName(config.getProjectName(), config.getBranchVersion());


        StringBuilder command = commandBuilder(Sys.GIT, GIT_PUBLISH_BASELINE);
        command.append(" ").append(config.getGitPath());
        command.append(" ").append(tagName);
        command.append(" ").append(baselineOfBranchName);
        command.append(" ").append(newestBaselineName);
        command.append(" ").append(branchName);
        command.append(" ").append(config.getNewStableVersion());
        command.append(" ").append(jdk);

        recordRunLog(Sys.GIT, ipAddr, userName, command.toString());

        ConsoleStatus status = executeInMachine(Sys.GIT, null, command.toString(), config);


        if (!status.isSuccess())
            throw new ScriptException("远程执行异常: " + status.getOutput());

        switch (status.getStatus()) {
            case 0:
                return;

            case 5:
                throw new ScriptException("如果你开发的分支所在的基线已经不是最新基线，发布时请先合并最新基线代码到你的分支，" +
                        "并手动打一tag,tag的命名规则为:模块名称_最新基线的四位版本_MergeTo_分支版本.例如：" +
                        config.getProjectName() + "_" + config.getNewestBaselineVersion() + "_MergeTo_" + config.getBranchVersion());
            case 9:
                throw new ScriptException("合并代码后，请重新创建四位版本编译测试通过后再发布");


            default:
                throw new ScriptException("创建Git基线发生错误: " + status.getOutput());
        }
    }

    /**
     * 调用脚本发布基线
     * @throws ScriptException
     */
    public static void createBaseline(BaselineConfig config) throws ScriptException {

        final String userName = Context.getUser();
        final String ipAddr = Context.getIp();

        String fullUrl = "/" + config.getRepoName() + config.getModuleUrl();
        String tagName = tagsName(config.getModuleName(), config.getTagVersion());
        String baselineOfBranchName = baselineName(config.getModuleName(), config.getBaselineOfBranchVersion());
        String newestBaselineName = baselineName(config.getModuleName(), config.getNewestBaselineVersion());
        String branchName = branchName(config.getModuleName(), config.getBranchVersion());

        StringBuilder command = commandBuilder(Sys.SVN, SVN_PUBLISH_BASELINE);
        command.append(" ").append(fullUrl);
        command.append(" ").append(tagName);
        command.append(" ").append(baselineOfBranchName);
        command.append(" ").append(newestBaselineName);
        command.append(" ").append(branchName);
        command.append(" ").append(config.getNewStableVersion());

        recordRunLog(Sys.SVN, ipAddr, userName, command.toString());

        ConsoleStatus status = executeInMachine(Sys.SVN, null, command.toString(), config);


        if (!status.isSuccess())
            throw new ScriptException("远程执行异常: " + status.getOutput());

        switch (status.getStatus()) {
            case 0:
                return;

            case 1:
                throw new ScriptException("tag可能不存在 [ " + tagName + " ]");

            case 2:
                throw new ScriptException("pom.xml文件的版本不是推荐的对外版本 [ " + config.getNewStableVersion() + " ]");

            case 3:
                throw new ScriptException("主干末端是否与tag一致");

            case 4:
                throw new ScriptException("已经发布的: " + tagName + "_MAIN 不是从主干创建的");

            case 5:
                throw new ScriptException("如果你开发的分支所在的基线已经不是最新基线，发布时请先合并最新基线代码到你的分支，" +
                        "并手动打一tag,tag的命名规则为:模块名称_最新基线的四位版本_MergeTo_分支版本.例如：" +
                        config.getModuleName() + "_" + config.getNewestBaselineVersion() + "_MergeTo_" + config.getBranchVersion());

            case 6:
                throw new ScriptException("如果你开发的分支所在的基线已经不是最新基线，发布时请先合并最新基线代码到你的分支，" +
                        "并手动打一tag,tag的命名规则为:模块名称_最新基线的四位版本_MergeTo_分支版本.例如：" + config.getModuleName()
                        + "_" + config.getNewestBaselineVersion() + "_MergeTo_" +
                        config.getBranchVersion() + ", 注意: " + config.getModuleName() + "_" + config.getNewestBaselineVersion()
                        + "_MergeTo_" + config.getBranchVersion()
                        + "必须从分支 [ " + branchName + " ] 创建");

            case 7:
                throw new ScriptException("主干与tag之间有代码差异");

            case 8:
                throw new ScriptException("基线不是从主干创建的，或者tag与存在基线存在diff");

            case 9:
                throw new ScriptException("合并代码后，请重新创建四位版本编译测试通过后再发布");

            default:
                throw new ScriptException("创建基线发生错误: " + status.getOutput());
        }
    }


    /**
     * 调用脚本发布基线
     * @throws ScriptException
     */
    public static void createBaselineWithJDK(BaselineConfig config, String jdk) throws ScriptException {

        final String userName = Context.getUser();
        final String ipAddr = Context.getIp();

        String fullUrl = "/" + config.getRepoName() + config.getModuleUrl();
        String tagName = tagsName(config.getModuleName(), config.getTagVersion());
        String baselineOfBranchName = baselineName(config.getModuleName(), config.getBaselineOfBranchVersion());
        String newestBaselineName = baselineName(config.getModuleName(), config.getNewestBaselineVersion());
        String branchName = branchName(config.getModuleName(), config.getBranchVersion());

        StringBuilder command = commandBuilder(Sys.SVN, SVN_PUBLISH_BASELINE);
        command.append(" ").append(fullUrl);
        command.append(" ").append(tagName);
        command.append(" ").append(baselineOfBranchName);
        command.append(" ").append(newestBaselineName);
        command.append(" ").append(branchName);
        command.append(" ").append(config.getNewStableVersion());
        command.append(" ").append(jdk);

        recordRunLog(Sys.SVN, ipAddr, userName, command.toString());

        ConsoleStatus status = executeInMachine(Sys.SVN, null, command.toString(), config);


        if (!status.isSuccess())
            throw new ScriptException("远程执行异常: " + status.getOutput());

        switch (status.getStatus()) {
            case 0:
                return;

            case 1:
                throw new ScriptException("tag可能不存在 [ " + tagName + " ]");

            case 2:
                throw new ScriptException("pom.xml文件的版本不是推荐的对外版本 [ " + config.getNewStableVersion() + " ]");

            case 3:
                throw new ScriptException("主干末端是否与tag一致");

            case 4:
                throw new ScriptException("已经发布的: " + tagName + "_MAIN 不是从主干创建的");

            case 5:
                throw new ScriptException("如果你开发的分支所在的基线已经不是最新基线，发布时请先合并最新基线代码到你的分支，" +
                        "并手动打一tag,tag的命名规则为:模块名称_最新基线的四位版本_MergeTo_分支版本.例如：" +
                        config.getModuleName() + "_" + config.getNewestBaselineVersion() + "_MergeTo_" + config.getBranchVersion());

            case 6:
                throw new ScriptException("如果你开发的分支所在的基线已经不是最新基线，发布时请先合并最新基线代码到你的分支，" +
                        "并手动打一tag,tag的命名规则为:模块名称_最新基线的四位版本_MergeTo_分支版本.例如：" + config.getModuleName()
                        + "_" + config.getNewestBaselineVersion() + "_MergeTo_" +
                        config.getBranchVersion() + ", 注意: " + config.getModuleName() + "_" + config.getNewestBaselineVersion()
                        + "_MergeTo_" + config.getBranchVersion()
                        + "必须从分支 [ " + branchName + " ] 创建");

            case 7:
                throw new ScriptException("主干与tag之间有代码差异");

            case 8:
                throw new ScriptException("基线不是从主干创建的，或者tag与存在基线存在diff");

            case 9:
                throw new ScriptException("合并代码后，请重新创建四位版本编译测试通过后再发布");

            default:
                throw new ScriptException("创建基线发生错误: " + status.getOutput());
        }
    }

    public static ConsoleStatus executeInMachine(Sys sys, Long tId, String command, Cfg cfg) throws ScriptException {
        String host = _usableIp;
        try {
            if(Diamond.isSystemInInsulateMode() && sys == Sys.SVN && !cfg.getPath().startsWith("ec/"))
                throw new ScriptException("警告,目前系统处于正式库隔离状态，在此状态时，您将不能操作除了[ec]外其他SVN系统库的脚本操作");
            if(Diamond.isSystemInInsulateMode() && sys == Sys.GIT && !cfg.getPath().startsWith("gscm/"))
                throw new ScriptException("警告,目前系统处于正式库隔离状态，在此状态时，您将不能操作除了[gscm]外其他Git系统库的脚本操作");
            return executeCommands(
                    sys,
                    tId,
                    Diamond.isInCompileMachineTestMode()? Diamond.getString("compile.machine.test.ip") : host,
                    Diamond.getString("gscm.script.user"),
                    Diamond.getInteger("gscm.script.port"),
                    command
            );
        } catch (DiamondException e) {
            logger.error(e.getMessage(), e);
        }
        throw new ScriptException("脚本执行中配置文件读取异常");
    }


    private static boolean fullDisk(String ip, MachineStatus machineStatus){
        String disk = null;
        try {
            disk = Diamond.getCurrDisk();
        } catch (DiamondException e) {
            logger.error(e.getMessage(), e);
        }
        if(machineStatus.getDiskUsagePercent().get(disk) != null
                && machineStatus.getDiskUsagePercent().get(disk) > 0.90){
            offline(ip);
            return true;
        }
        return false;
    }

    private static String faultTolerance() throws Exception{
        if(SESSION.size() == 0){
            if(LAST_SUCCESS_SERVER.size() == 1) {
                Map.Entry<String, String> lastAvailableServer = LAST_SUCCESS_SERVER.poll();
                if(lastAvailableServer != null)
                    initSession(true, lastAvailableServer.getKey(),
                            "No Available Server, Start Fault Tolerance, Last Available Server Enable: " +
                                    lastAvailableServer.getKey() + " at "+ lastAvailableServer.getValue()+" ", lastAvailableServer.getValue());
                return lastAvailableServer != null ? lastAvailableServer.getKey() : null;
            }
        }else
            //return a available server
            return SESSION.keySet().iterator().next();
        return null;
    }

    public static Map<Long, CompileWork> getWork(Sys sys) throws ScriptException {
        switch (sys){
            case GIT:
                return gitWork;
            case SVN:
                return work;
            default:
                throw new ScriptException("无法识别的系统: " + sys);
        }
    }

    public static CompileWork getWork(Sys sys, Long tId) throws ScriptException {
        switch (sys){
            case GIT:
                if(gitWork.get(tId) != null)
                    return gitWork.get(tId);
                else
                    throw new ScriptException("服务器异常, 无法获取: " + sys + ":" + tId);
            case SVN:
                if(work.get(tId) != null)
                    return work.get(tId);
                else
                    throw new ScriptException("服务器异常, 无法获取: " + sys + ":" + tId);
            default:
                throw new ScriptException("无法识别的系统: " + sys);
        }
    }


    static class OutputControl implements Runnable{
        private String host;
        private String user;
        private int port;
        private String command;

        private long start_;

        private AtomicBoolean b;
        private AtomicBoolean closeable;
        private AtomicBoolean outputCheck;

        private StringBuilder output;
        private Shell shell;

        public OutputControl(String host, String user, int port, String command, long start_,
                             AtomicBoolean b, AtomicBoolean closeable, AtomicBoolean outputCheck,
                             StringBuilder output, Shell shell) throws ScriptException {
            this.host = host;
            this.user = user;
            this.port = port;
            this.command = command;
            this.start_ = start_;
            this.b = b;
            this.closeable = closeable;
            this.outputCheck = outputCheck;
            if(output == null)
                throw new ScriptException("输出为NULL");
            this.output = output;
            this.shell = shell;
        }

        @Override
        public void run() {
            try {
                while (output.length() == 0 && (System.currentTimeMillis() - start_) < 10000L && !b.get() && !closeable.get()){
                    Thread.sleep(100);
                }
                if(output.length() == 0){
                    if(shell.getOpenChannel().isConnected())
                        shell.getOpenChannel().disconnect();
                    outputCheck.set(true);
                    logger.error("[ " + user + "@" + host + ":" + port + " ] 脚本输出日志为空(超过10秒): " + command);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 脚本执行超时控制
     */
    static class TimeoutControl implements Runnable{

        private String host;
        private String user;
        private int port;
        private String command;

        private long start_;
        private long maxWaitTime;
        private AtomicBoolean b;
        private AtomicBoolean closeable;
        private AtomicBoolean timeout;
        private AtomicBoolean outputCheck;
        private Shell shell;

        TimeoutControl(String host, String user, int port, String command, long start_, long maxWaitTime,
                              AtomicBoolean b, AtomicBoolean closeable, AtomicBoolean timeout,
                       AtomicBoolean outputCheck, Shell shell) {
            this.host = host;
            this.user = user;
            this.port = port;
            this.command = command;
            this.start_ = start_;
            this.maxWaitTime = maxWaitTime;
            this.b = b;
            this.closeable = closeable;
            this.timeout = timeout;
            this.outputCheck = outputCheck;
            this.shell = shell;
        }

        @Override
        public void run() {
            try {
                while ((System.currentTimeMillis() - start_) < maxWaitTime && !b.get() && !closeable.get() && !outputCheck.get()) {
                    Thread.sleep(100);
                }
                if(closeable.get() || b.get() || outputCheck.get()){
                    shell.getOpenChannel().disconnect();
                    return;
                }
                logger.error("[ " + user + "@" + host + ":" + port + " ] 脚本执行超时: " + command);
                if(shell.getOpenChannel().isConnected())
                    shell.getOpenChannel().disconnect();
                timeout.set(true);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    static class LastServerControl implements Runnable{

        private String host;

        LastServerControl(String host) {
            this.host = host;
        }

        @Override
        public void run() {
            if (LAST_SUCCESS_SERVER.size() == 1) {
                LAST_SUCCESS_SERVER.poll();
            }
            LAST_SUCCESS_SERVER.offer(new Map.Entry<String, String>() {
                private String key = host;
                private String value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                @Override
                public String getKey() {
                    return key;
                }

                @Override
                public String getValue() {
                    return value;
                }

                @Override
                public String setValue(String value) {
                    this.value = value;
                    return value;
                }
            });
        }

    }

    private static void buildLog(Sys sys, Long tId, StringBuilder logContent) throws ScriptException {
        try {
            if(tId == null)
                return;
            CompileWork compile = getWork(sys, tId);
            if (compile == null) {
                return;
            }
            StringReader sr = new StringReader(compile.getCompileLog().toString());
            LineNumberReader lr = new LineNumberReader(sr);
            lr.skip(Long.MAX_VALUE);
            int x = lr.getLineNumber();
            x++;
            sr.close();
            lr.close();
            if (x < 30) {
                logContent.append(compile.getCompileLog());
                return;
            }
            logContent.append(".........");
            sr = new StringReader(compile.getCompileLog().toString());
            BufferedReader br = new BufferedReader(sr);
            int k = 0;
            String line;
            while ((line = br.readLine()) != null) {
                if (k > (x - 20))
                    logContent.append("\n").append(line);
                k++;
            }
            sr.close();
            br.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void compileLog(Sys sys, Long tId, String log) throws ScriptException {
        if (tId != null) {
            CompileWork compile = getWork(sys, tId);
            if(compile != null)
                compile.appendLog(log);
        }
    }

    /**
     * 远程 执行命令并返回结果调用过程 是同步的（执行完才会返回）
     *
     * @param hostTarget    主机名
     * @param user    用户名
     * @param port    端口
     * @param command 命令
     * @return
     */
    public static ConsoleStatus  executeCommands(Sys sys,
                                                Long tId,
                                                String hostTarget,
                                                final String user,
                                                final int port,
                                                final String command) throws ScriptException {
        String host = hostTarget;
        ConsoleStatus status = new ConsoleStatus(sys);
        if(host == null || "".equals(host)){
            status.setSuccess(false);
            status.setOutput("无可用服务器，请稍后执行");
            compileLog(sys, tId, "无可用服务器，请稍后执行");
            return status;
        }

        final long maxWaitTime, start_ = System.currentTimeMillis();
        boolean remoteQuitTimeout = false;
        final AtomicBoolean
                //脚本执行退出标志
                b,
                //脚本执行过程中超时
                timeout,
                //用户手动关闭
                closeable,
                //检查10秒内有无输出
                outputCheck;
        //脚本调试
        boolean scriptDebug;
        try {
            closeable = new AtomicBoolean();
            timeout = new AtomicBoolean();
            b = new AtomicBoolean();
            outputCheck = new AtomicBoolean();
            scriptDebug = Diamond.getInteger("gscm.script.debug") == 1;
            maxWaitTime = Diamond.getLong("gscm.script.timeout") * 1000L;
        } catch (DiamondException e) {
            logger.error(e.getMessage(), e);
            return status;
        }
        if (scriptDebug) {
            status.setSuccess(true);
            status.setStatus(0);
            status.setOutput("");
            return status;
        }
        StringBuilder result = new StringBuilder();
        final Shell shell = new Shell();
        ChannelShell openChannel = null;
        if (tId != null) {
            CompileWork compile = getWork(sys, tId);
            if(compile != null)
                compile.setCloseable(closeable);
        }
        Session session = null;
        try {
            if(SESSION.get(host) == null){
                boolean isFaultTolerance = Diamond.getInteger("gscm.script.fault.tolerance") == 1;
                if(isFaultTolerance)
                    host = faultTolerance();
                else{
                    status.setSuccess(false);
                    status.setOutput("当前服务器 " + host + " 不可用，请稍后执行");
                    compileLog(sys, tId, "当前服务器 " + host + " 不可用，请稍后执行");
                    return status;
                }
            }
            logger.info("[ " + user + "@" + host + ":" + port + " ] 正在远程执行: " + command);
            session = SESSION.get(host).borrowObject();
            if (session == null){
                status.setSuccess(false);
                status.setOutput("当前服务器 " + host + " 不可用，请稍后执行");
                compileLog(sys, tId, "当前服务器 " + host
                        + " 不可用，请稍后执行");
                return status;
            }
            openChannel = (ChannelShell) session.openChannel("shell");
            shell.setOpenChannel(openChannel);
            openChannel.connect();

            if (!openChannel.isConnected()) {
                throw new ScriptException("远程服务器无法连接: " + host);
            }
            OutputStream outputStream = openChannel.getOutputStream();
            String _command = command + "\n";
            outputStream.write(_command.getBytes());
            outputStream.flush();
            outputStream.write("exit\n".getBytes());
            outputStream.flush();
            outputStream.close();

            final InputStream in = openChannel.getInputStream();
            final InputStreamReader insr = new InputStreamReader(in);
            final BufferedReader reader = new BufferedReader(insr);
            String buf;
            int i = 0;
            _EXE.execute(new OutputControl(host, user, port, command, start_, b, closeable, outputCheck,
                    result, shell));
            _EXE.execute(new TimeoutControl(host, user, port, command, start_, maxWaitTime, b, closeable,
                    timeout, outputCheck, shell));
            while ((buf = reader.readLine()) != null) {
                if (TrashMessage.isTrashMessage(buf, command))
                    continue;
                if (i > 0)
                    result.append('\n');
                result.append(buf);
                //MBProgress
                if (!buf.startsWith("Progress")){
                    compileLog(sys, tId, buf);
                }else {
                    continue;
                }
                i++;
            }
            long maxWait = 10000L;
            long start = System.currentTimeMillis();
            while (!openChannel.isClosed() && (System.currentTimeMillis() - start) <= maxWait) {
                Thread.sleep(10);
            }
            remoteQuitTimeout = System.currentTimeMillis() - start > maxWait;
            if (remoteQuitTimeout || timeout.get() || closeable.get() || outputCheck.get()) {
                status.setOutput(
                        remoteQuitTimeout? "远程退出指令执行超时, 请重新执行":
                                (timeout.get()? "脚本执行超时: " + command + ", 配置时间: " + Diamond.getInteger("gscm.script.timeout") + "秒, 请重新执行": (
                                        closeable.get()?"脚本执行被手动终止": ( outputCheck.get()? "脚本输出日志为空(超过10秒), 请重新执行" : "发生未知错误, 请重新执行")
                                        )));
                compileLog(sys, tId, remoteQuitTimeout? "远程退出指令执行超时, 请重新执行":
                        (timeout.get()? "脚本执行超时: " + command + ", 配置时间: " + Diamond.getInteger("gscm.script.timeout") + "秒, 请重新执行": (
                                closeable.get()?"脚本执行被手动终止" : ( outputCheck.get()? "脚本输出日志为空(超过10秒), 请重新执行" : "发生未知错误, 请重新执行")
                        )));
            } else {
                int exitStatus = openChannel.getExitStatus();
                status.setStatus(exitStatus);
                status.setSuccess(true);
                status.setOutput(result.toString());
            }
            in.close();
            insr.close();
            reader.close();
            b.set(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.append(e.getMessage());
        } finally {
            if (openChannel != null && !openChannel.isClosed()) {
                openChannel.disconnect();
            }
            openChannel = null;
            try {
                if (session != null && SESSION.get(host) != null) {
                   SESSION.get(host).returnObject(session);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        if(remoteQuitTimeout || timeout.get() || outputCheck.get()){
            //临时解决策略，重新建立连接池
            offline(host);
            try {
                online(host);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        StringBuilder logContent = new StringBuilder();
        buildLog(sys, tId, logContent);
        logger.info("[ " + user + "@" + host + ":" + port + " ] 远程执行结果: " + (tId == null ? status : "\n" + logContent.toString()));
        if(status.isSuccess() && status.getStatus() == 0) {
            _EXE.execute(new LastServerControl(host));
        }
        return status;
    }

    private static void initSession(String machineIp) throws Exception{
        initSession(false, machineIp, "Initializing GSCM Compile Server: " + machineIp, null);
    }

    private static void initSession(boolean recover, String machineIp, String info, String date) throws Exception {
        if(recover){
            logger.error(info);
            _usableIp = machineIp;
            _rank = "警告, 系统开始容错处理，所有的请求将由 [ "+machineIp+" ] 完成 at " + date;
            _status = "<tr><td colspan=\"5\" align=\"center\">警告, 系统开始容错处理，所有的请求将由 [ "+machineIp+" ] 完成 at " + date + "</td></tr>";
            Diamond.setNotice("<span style=\"color: red\">警告, 系统开始容错处理，所有的请求将由 [ "+machineIp+" ] 完成，如果你看到此条消息，请联系管理员</span>");
        }else
            logger.info(info);
        //初始化Session
        if(callAmount.get(machineIp) == null)
            callAmount.put(machineIp, new AtomicLong(0L));
        if(SESSION.get(machineIp)    == null) {
            GenericObjectPool<Session> pool = new GenericObjectPool<>(new SessionPoolableObjectFactory(
                    machineIp,
                    Diamond.getString("gscm.script.user"),
                    Secure.decrypt(Diamond.getString("gscm.script.password"))
            ), 20);
            pool.setTestOnBorrow(true);
            pool.setTestOnReturn(true);
            SESSION.put(machineIp, pool);
        }
    }

    private static String toStatusLine(Collection<MachineStatus> ls){
        StringBuilder s = new StringBuilder();
        for(MachineStatus ms : ls){
            s.append("<tr>");
            s.append("<td align='center'>").append(ms.getIp()).append("</td>");
            BigDecimal b =
                    BigDecimal.valueOf(ms.getCpuSysUsagePercent());
            double f1 =
                    b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            s.append("<td align='center'>").append(f1).append("%").append("</td>");

            b = BigDecimal.valueOf(ms.getCpuUserUsagePercent());
            f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            s.append("<td align='center'>").append(f1).append("%").append("</td>");

            b = BigDecimal.valueOf(ms.getMemoryUsagePercent()*100.0);
            f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            s.append("<td align='center'>").append(f1).append("%").append("</td>");

            s.append("<td>");

            s.append("<div>");
            s.append("<table class='disk' style='width:100%'>");
            for(String k : ms.getDiskUsagePercent().keySet()){
                b = BigDecimal.valueOf(ms.getDiskUsagePercent().get(k)*100.0);
                f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                s.append("<tr>").append("<td>").append(k).append("</td><td>").append(f1).append("%</td>").append("</tr>");
            }
            s.append("</table>");
            s.append("</div>");

            s.append("</td>");
            s.append("</tr>");
        }
        return s.toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("初始化GSCM脚本服务...");
        pickRules.add(new CpuPickRule());
        pickRules.add(new MemoryPickRule());
        pickRules.add(new DiskPickRule());
        if(cacheService != null) {
            byte[] val;
            if ((val = cacheService.getBytes("GSCM_CALL_AMOUNT_CACHE_")) != null) {
                JSONObject callAmountMap = new JSONObject();
                ProtostuffIOUtil.mergeFrom(val, callAmountMap, schema);
                for (String k : callAmountMap.keySet()) {
                    callAmount.put(k, new AtomicLong(callAmountMap.getLong(k)));
                }
            }
            if ((val = cacheService.getBytes("GSCM_PEOPLE_AMOUNT_CACHE_")) != null) {
                JSONObject peopleAmountMap = new JSONObject();
                ProtostuffIOUtil.mergeFrom(val, peopleAmountMap, schema);
                for (String k : peopleAmountMap.keySet()) {
                    peopleAmount.put(k, new AtomicLong(peopleAmountMap.getLong(k)));
                }
            }
        }
        if(!Context.isTestEnvironment())
            _EXE.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        zookeeper.start();
                        PathChildrenCache cache = new PathChildrenCache(zookeeper, "/dubbo/"+ConsoleApi.class.getName()+"/consumers", false);
                        cache.start();
                        cache.getListenable().addListener(Exec.this);
                        zkLatch.await();
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            });
        logger.info("初始化GSCM脚本服务...ok");
    }

    @Override
    public void destroy() throws Exception {
        logger.info("GSCM脚本服务 Shutting Down......");
        EXECUTOR.shutdown();
        _EXE.shutdown();
        zookeeper.close();
        zkLatch.countDown();
    }

    @Override
    public String getDiskList() {
        try {
            return Diamond.getString("gscm.script.disk.list");
        } catch (DiamondException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

    private static void recordInner(String ip, MachineStatus machineStatus) {
        if(fullDisk(ip, machineStatus)){
            logger.error("GSCM Compile Server [ "+ip+" ] DISK FULL !!!!!!!! ");
        }else
            machineStatusMap.put(ip, machineStatus);
        pickLock.lock();
        try {
            _status = toStatusLine(machineStatusMap.values());
            StringBuilder rankBuffer = new StringBuilder();
            String selectIp = null;
            for (IExecRule rule : pickRules) {
                RuleResult select = rule.pick(machineStatusMap.values());
                rankBuffer.append(",").append(select.getRank());
                if (!select.isSame()) {
                    selectIp = select.getIp();
                    break;
                }
            }
            if (selectIp != null) {
                _usableIp = selectIp;
                _rank = rankBuffer.length() > 1 ? rankBuffer.substring(1) : "";
            }else
                _usableIp = machineStatusMap.values().iterator().next().getIp();
        } finally {
            pickLock.unlock();
        }
    }

    @Override
    public void record(String ip, MachineStatus machineStatus) throws ConsoleException {
        recordInner(ip, machineStatus);
    }

    public static void online(String host) throws Exception {
        logger.info("GSCM Compile Server Found: " + host);
        initSession(host);
    }

    public static void offline(String host){
        logger.info("GSCM Compile Server Offline: " + host);
        try {
            SESSION.get(host).close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        SESSION.remove(host);
        machineStatusMap.remove(host);
    }

    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        String urlStr;
        URL url;
        switch ( event.getType() )
        {
            case CHILD_ADDED:
                urlStr = URLDecoder.decode(ZKPaths.getNodeFromPath(event.getData().getPath()), "UTF-8");
                url = URL.valueOf(urlStr);
                online(url.getHost());
                break;
            case CHILD_REMOVED:
                urlStr = URLDecoder.decode(ZKPaths.getNodeFromPath(event.getData().getPath()), "UTF-8");
                url = URL.valueOf(urlStr);
                offline(url.getHost());
                break;
            default:
                break;
        }
    }
}