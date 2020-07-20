package org.nh.core.web.filter.security;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by root on 11/11/17.
 */
public class IpUtils {

    /**
     * 获取远程访问IP
     *
     * @return
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String remoteIp = null;
        remoteIp = request.getHeader("X-Forwarded-For");
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("X-Real-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("Proxy-Client-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("HTTP_CLIENT_IP");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getRemoteAddr();
        }
        if (remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getRemoteHost();
        }
        // 如果是多级代理，那么取第一个ip为客户ip
        if (remoteIp != null && remoteIp.indexOf(",") != -1) {
            remoteIp = remoteIp.substring(0, remoteIp.indexOf(",")).trim();
        }
        return remoteIp;
    }
}