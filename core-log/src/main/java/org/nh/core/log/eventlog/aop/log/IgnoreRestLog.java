package org.nh.core.log.eventlog.aop.log;

import java.lang.annotation.*;

/**
 * @Classname IgnoreRestLog
 * @Description TODO 标记了该注解的类或者方法不会被记录日志
 * @Date 2019/10/16 4:12 PM
 * @Created by nihui
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE,ElementType.METHOD})
@Documented
public @interface IgnoreRestLog {
}
