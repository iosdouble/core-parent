package org.nh.core.log.contants;

/**
 * @Classname LogAppenderName
 * @Description TODO 用于输出日志的分类
 * @Date 2019/10/16 4:09 PM
 * @Created by nihui
 */
public class LogAppenderName {
    /**
     * juran核心框架输出日志
     */
    public static final String CORE="nh.core";
    /**
     * 业务系统输出日志
     */
    public static final String INFO="nh.info";
    /**
     * API event输出日志
     */
    public static final String EVENT="nh.event";
    /**
     * API event输出日志
     */
    public static final String AUDIT="nh.audit";
    /**
     * 业务运行时异常输出日志
     */
    public static final String EXCEPTION_BUSINESS="nh.business.exception";

    /**
     * 系统异常时输出日志
     */
    public static final String EXCEPTION_SYSTEM="nh.system.exception";

    /**
     * Feign的日志输出
     */
    public static final String FEIGN="nh.feign";
}
