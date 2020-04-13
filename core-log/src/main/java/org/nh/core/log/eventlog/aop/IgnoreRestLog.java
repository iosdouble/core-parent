package org.nh.core.log.eventlog.aop;

import java.lang.annotation.*;

/**
 * @Classname IgnoreRestLog
 * @Description TODO 标注了该注解的类或者方法，不会被记录日志
 * @Date 2020/4/13 6:42 PM
 * @Created by nihui
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE,ElementType.METHOD})
@Documented
public @interface IgnoreRestLog {
}
