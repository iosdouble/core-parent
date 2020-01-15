package org.nh.core.util.cache;


import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @Classname RedisUtil
 * @Description TODO Redis操作CRUD的工具类
 * @Date 2020/1/15 1:42 PM
 * @Created by nihui
 */
public class RedisUtil {

    /**
     * 如果Bean 不为null 会自动转换为用户想要的Bean，但是如果类型无法转换，就会抛出运行时异常
     * @param redisTemplate
     * @param key
     * @param <T>
     * @return 通过RedisTemple 获取Key 对应的Bean
     */
    public static<T> T getBean(RedisTemplate<String,Object> redisTemplate,Object key){
        Object obj = redisTemplate.opsForValue().get(key);
        T t = null;
        if (obj!=null){
            t = (T) obj;
        }
        return t;
    }

    /**
     * Redis中存入一个对象，并且没有时间限制
     * @param redisTemplate
     * @param key
     * @param value
     * @param <T>
     */
    public static<T> void setBean(RedisTemplate<String,Object> redisTemplate,String key ,T value){
        redisTemplate.opsForValue().set(key,value);
    }

    /**
     * 向Redis中存入一个对象，并且设置时间限制，如果time为小于等于0的值，默认时间为不限制，单位为秒
     * @param redisTemplate
     * @param key
     * @param value
     * @param time
     * @param <T>
     */
    public static<T> void setBeasn(RedisTemplate<String,Object> redisTemplate,String key,T value,long time){
        if (time<=0){
            setBean(redisTemplate,key,value);
            return;
        }
        redisTemplate.opsForValue().set(key,value,time,TimeUnit.SECONDS);
    }
}
