package org.nh.core.log.eventlog.aop.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * nihui
 *
 * Controller日志记录
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerEndpoint {
    String operation() default "";
    String exceptionMessage() default "系统内部异常";
}
