package org.nh.core.log.contents;

/**
 * @Classname LogAppenderName
 * @Description TODO 输出日志分类
 * @Date 2020/4/13 6:34 PM
 * @Created by nihui
 */
public class LogAppenderName {

    /**
     * juran核心框架输出日志
     */
    public static final String CORE="core";
    /**
     * 业务系统输出日志
     */
    public static final String INFO="info";
    /**
     * API event输出日志
     */
    public static final String EVENT="event";
    /**
     * API event输出日志
     */
    public static final String AUDIT="audit";
    /**
     * 业务运行时异常输出日志
     */
    public static final String EXCEPTION_BUSINESS="business.exception";

    /**
     * 系统异常时输出日志
     */
    public static final String EXCEPTION_SYSTEM="system.exception";

    /**
     * Feign的日志输出
     */
    public static final String FEIGN="feign";
}
