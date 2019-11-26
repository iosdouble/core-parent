package org.nh.core.log.eventlog.aop.log;

import java.lang.annotation.*;

/**
 * @Classname RestLog
 * @Description TODO 标记了该注解的类或者方法会被记录RestLog
 * @Date 2019/10/16 4:12 PM
 * @Created by nihui
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE,ElementType.METHOD})
@Documented
public @interface RestLog {

    /**
     * 描述
     * @return
     */
    String description() default "";
}
