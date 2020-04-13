package org.nh.core.util.map;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname MapUtil
 * @Description TODO
 * @Date 2020/1/15 2:50 PM
 * @Created by nihui
 */
public class MapUtil {

    /**
     * 删除Map中对应的key的项 注意：返回的map和作为参数传入的map不是一个对象
     * @param map 需要操作的map
     * @param removeKeyArray 需要删除的key的数组
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> removeEntities(Map<String, T> map, String[] removeKeyArray) {
        Map<String, T> newMap = new HashMap<String, T>();
        if (removeKeyArray != null && removeKeyArray.length > 0) {
            for (Map.Entry<String, T> entry : map.entrySet()) {
                boolean isRecord=true;
                for (int i = 0; i < removeKeyArray.length; i++) {
                    String removeKey = removeKeyArray[i];
                    if (entry.getKey().equals(removeKey)) {
                        isRecord=false;
                        break;
                    }
                }
                if(isRecord){
                    newMap.put(entry.getKey(), entry.getValue());
                }
            }
        } else {
            newMap.putAll(map);
        }

        return newMap;
    }
}
