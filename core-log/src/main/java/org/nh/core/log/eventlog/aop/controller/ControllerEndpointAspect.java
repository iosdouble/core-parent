package org.nh.core.log.eventlog.aop.controller;


import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.nh.core.log.eventlog.aop.AspectSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author MrBird
 */
@Aspect
@Component
public class ControllerEndpointAspect extends AspectSupport {


    @Pointcut("@annotation(org.nh.core.log.eventlog.aop.controller.ControllerEndpoint)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws RuntimeException {
        Object result;
        Method targetMethod = resolveMethod(point);
        ControllerEndpoint annotation = targetMethod.getAnnotation(ControllerEndpoint.class);
        String operation = annotation.operation();
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
        return null;
    }
}



