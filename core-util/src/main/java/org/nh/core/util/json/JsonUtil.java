package org.nh.core.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.apache.commons.lang3.StringUtils;
import org.nh.core.util.json.exception.JsonUtilErrorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname JsonUtil
 * @Description TODO Json 数据格式处理类
 * @Date 2019/10/16 3:48 PM
 * @Created by nihui
 */
public class JsonUtil {


    /**
     * Bean转换成JSON字符串
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String toJson(T t) {
        if(t==null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonUtilErrorException("toJson转换错误", e);
        }
        return json;
    }


    /**
     * Bean转换为JSON串的同时将 Long转换成String
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String toJsonAndLongToString(T t) {
        if(t==null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        /**
         * 序列换成json时,将所有的long变成string
         * 因为js中得数字类型不能包含所有的java long值
         */
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        mapper.registerModule(simpleModule);
        String json = null;
        try {
            json = mapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonUtilErrorException("toJsonAndLongToString转换错误", e);
        }
        return json;
    }


    /**
     * 根据Path将JSON串中的某一部分转换为Bean
     * @param json
     * @param path
     * @param clazz
     * @param <T>
     * @return
     */

    public static <T> T toObject(String json, String path, Class<T> clazz) {
        if(StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        T t = null;
        if (StringUtils.isBlank(path)) {
            try {
                t = mapper.readValue(json, clazz);
            } catch (IOException e) {
                e.printStackTrace();
                throw new JsonUtilErrorException("toObject转换错误", e);
            }
            return t;
        }
        try {
            JsonNode jsonRoot = mapper.readTree(json);
            JsonNode jsonNode = jsonRoot.at(path);
            if (jsonNode.isMissingNode()) {
                return t;
            }
            t = mapper.readValue(jsonNode.toString(), clazz);
        }  catch (IOException e) {
            e.printStackTrace();
            throw new JsonUtilErrorException("toObject转换错误", e);
        }
        return t;
    }


    /**
     * 将JSON转换为Bean
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        if(StringUtils.isBlank(json)) {
            return null;
        }
        return toObject(json, null, clazz);
    }

    /**
     * 将JSON字符串转换为对象数组
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */

    public static <T> List<T> toObjectList(String json, Class<T> clazz) {
        if(StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
        try {
            List<T> tList = mapper.readValue(json, javaType);
            return tList;
        } catch (IOException e) {
            e.printStackTrace();
            throw new JsonUtilErrorException("toObjectList转换错误", e);
        }
    }


    /***
     * 将JSON按照Path转换为Bean对象的List
     * @param json
     * @param path
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> toObjectList(String json, String path, Class<T> clazz) {
        if(StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        if (StringUtils.isBlank(path)) {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
            try {
                List<T> tList = mapper.readValue(json, javaType);
                return tList;
            } catch (IOException e) {
                e.printStackTrace();
                throw new JsonUtilErrorException("toObjectList转换错误", e);
            }
        } else {
            try {
                JsonNode jsonRoot = mapper.readTree(json);
                JsonNode jsonNode = jsonRoot.at(path);
                if (jsonNode.isMissingNode()) {
                    return null;
                }
                JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
                List<T> tList = mapper.readValue(jsonNode.toString(), javaType);
                return tList;
            } catch (IOException e) {
                e.printStackTrace();
                throw new JsonUtilErrorException("toObjectList转换错误", e);
            }
        }
    }

}
