package org.nh.core.exception.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @Classname IException
 * @Description TODO
 * @Date 2020/1/15 3:30 PM
 * @Created by nihui
 */
@JsonIgnoreProperties(value = {"cause","stackTrace","message","localizedMessage","suppressed"})
public interface IException {
    /**
     * 异常代码
     * @return
     */
    String getCode();

    /**
     * 异常消息
     * @return
     */
    String getMsg();
}
