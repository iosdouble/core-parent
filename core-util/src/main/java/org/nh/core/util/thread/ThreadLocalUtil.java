package org.nh.core.util.thread;

/**
 * @Classname ThreadLocalUtil
 * @Description TODO ThreadLocal工具类，单例模式
 * @Date 2020/1/15 3:01 PM
 * @Created by nihui
 */
public class ThreadLocalUtil {

    private ThreadLocalUtil(){}

    private static ThreadLocal<Object> t = new ThreadLocal<Object>();

    public static ThreadLocal<Object> getInstance() {
        return t;
    }
}
