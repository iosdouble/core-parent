package org.nh.core.util.console;

/**
 * Created by root on 8/14/17.
 */
public interface ConsoleApi {
    /**
     * 获取硬盘监视信息
     * @return
     */
    String getDiskList();

    /**
     * 记录监视日志
     * @param ip
     * @param machineStatus
     * @throws Exception
     */
    void record(String ip, MachineStatus machineStatus) throws ConsoleException;
}
