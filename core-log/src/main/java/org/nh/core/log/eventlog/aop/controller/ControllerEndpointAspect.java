package org.nh.core.log.eventlog.aop.controller;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.nh.core.log.eventlog.aop.AspectSupport;
import org.nh.core.log.logserver.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author nihui
 */
@Aspect
@Component
public class ControllerEndpointAspect extends AspectSupport {

    @Autowired
    private ILogService logService;

    @Pointcut("@annotation(org.nh.core.log.eventlog.aop.controller.ControllerEndpoint)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws RuntimeException {
        Object result;
        Method targetMethod = resolveMethod(point);
        ControllerEndpoint annotation = targetMethod.getAnnotation(ControllerEndpoint.class);
        String operation = annotation.operation();
        String exceptionMessage = annotation.exceptionMessage();
        long start = System.currentTimeMillis();
//        try {
//            result = point.proceed();
//            if (StringUtils.isNotBlank(operation)) {
//                HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
//                logService.saveLog(point, targetMethod, request, operation, start);
//            }
//            return result;
//        } catch (Throwable throwable) {
//            String exceptionMessage = annotation.exceptionMessage();
//            String message = throwable.getMessage();
//            String error = FebsUtil.containChinese(message) ? exceptionMessage + "，" + message : exceptionMessage;
//            throw new FebsException(error);
//        }

        try {
            result = point.proceed();
            logService.saveLog(point,targetMethod,exceptionMessage,operation,start);
            return result;
        }catch (Throwable throwable){
            throw new RuntimeException(throwable);
        }
    }

}



