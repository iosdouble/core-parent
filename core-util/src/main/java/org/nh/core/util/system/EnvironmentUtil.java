package org.nh.core.util.system;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Classname EnvironmentUtil
 * @Description TODO
 * @Date 2020/1/15 3:00 PM
 * @Created by nihui
 */
public class EnvironmentUtil {

    /**
     * 获取Liunx的hostName
     * @return
     */
    public static String getHostNameForLiunx() {
        try {
            return (InetAddress.getLocalHost()).getHostName();
        } catch (UnknownHostException uhe) {
            String host = uhe.getMessage(); // host = "hostname: hostname"
            if (host != null) {
                int colon = host.indexOf(':');
                if (colon > 0) {
                    return host.substring(0, colon);
                }
            }
            return "UnknownHost";
        }
    }


    /**
     * 获取主机hostName，兼容windows和liunx
     * @return
     */
    public static String getHostName() {
        if (System.getenv("COMPUTERNAME") != null) {
            return System.getenv("COMPUTERNAME");
        } else {
            return getHostNameForLiunx();
        }
    }
}
