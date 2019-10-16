package org.nh.core.util.json.exception;

/**
 * @Classname JsonUtilErrorException
 * @Description TODO 为了能让这个异常 在运行时被捕获所以继承了RuntimeException运行时异常类
 * @Date 2019/10/16 3:44 PM
 * @Created by nihui
 */
public class JsonUtilErrorException extends RuntimeException {

    public JsonUtilErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
