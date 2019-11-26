package org.nh.core.log.eventlog.aop.log;

import org.aspectj.lang.annotation.Aspect;
import org.nh.core.log.contants.LogAppenderName;
import org.nh.core.log.eventlog.aop.AspectSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Classname RestLogAspect
 * @Description TODO Rest log的切入类
 * @Date 2019/10/16 4:12 PM
 * @Created by nihui
 */
@Component
@Aspect
public class RestLogAspect extends AspectSupport {

    protected final transient Logger eventlogger = LoggerFactory.getLogger(LogAppenderName.EVENT);
    protected final transient Logger systemExceptionlogger = LoggerFactory.getLogger(LogAppenderName.EXCEPTION_SYSTEM);




}
