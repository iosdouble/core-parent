package org.nh.core.web.filter.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by root on 2/6/17.
 */
public final class Context {

    /**
     * user = 用户域账户登录名
     */
    private static final ThreadLocal<ConcurrentHashMap<String, Object>> LOCAL = new ThreadLocal<>();

    public static String getIp(){
        return (String) get("ipAddr");
    }

    public static String getUser() {
        return (String) get("username");
    }

    public static Map<String, Object> get() {
        return LOCAL.get();
    }


    public static boolean isTestEnvironment(){
        if (LOCAL.get() == null)
            return false;
        return LOCAL.get().get("Test") != null && (Boolean) LOCAL.get().get("Test");
    }

    public static Object get(String key) {
        if (LOCAL.get() == null)
            return null;
        return LOCAL.get().get(key);
    }

    public static void set(String key, Object value) {
        if (LOCAL.get() == null) {
            ConcurrentHashMap<String, Object> m = new ConcurrentHashMap<>();
            m.put(key, value);
            LOCAL.set(m);
        } else
            LOCAL.get().put(key, value);
    }

}
