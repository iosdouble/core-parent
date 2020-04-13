package org.nh.core.log.eventlog.aop;

import java.lang.annotation.*;

/**
 * @Classname RestLog
 * @Description TODO 标注了该注解的类或者方法会被记录事件日志
 * @Date 2020/4/13 6:42 PM
 * @Created by nihui
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE,ElementType.METHOD})
@Documented
public @interface RestLog {


    /**
     * 注解描述
     * @return
     */
    public String description() default "";
}
