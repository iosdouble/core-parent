package org.nh.core.log.logserver;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @Classname ILogService
 * @Description TODO
 * @Date 2019/11/26 5:12 PM
 * @Created by nihui
 */
public interface ILogService<T> extends IService<T> {

    /**
     * 查询操作日志分页
     *
     * @param systemLog 日志
     * @param request   QueryRequest
     * @return IPage<SystemLog>
     */
    IPage<T> findLogs(T systemLog, T request);

    /**
     * 删除操作日志
     *
     * @param logIds 日志 ID集合
     */
    void deleteLogs(String[] logIds);

    /**
     * 异步保存日志操作
     * @param point
     * @param method
     * @param object
     * @param operation
     * @param start
     */
    @Async("LOGSERVICE")
    void saveLog(ProceedingJoinPoint point, Method method, Object object, String operation, long start);
}
