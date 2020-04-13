package org.nh.core.util.id;

import java.util.UUID;

/**
 * @Classname UUIDUtil
 * @Description TODO UUID生成id的工具类
 * @Date 2020/1/15 2:45 PM
 * @Created by nihui
 */
public class UUIDUtil {

    /**
     * 获取一个本实例唯一的id
     * @return
     */
    public static String getId() {
        String id=UUID.randomUUID().toString();
        return id;
    }
}
