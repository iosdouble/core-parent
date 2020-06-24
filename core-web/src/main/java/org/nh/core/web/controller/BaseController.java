package org.nh.core.web.controller;

import lombok.Data;
import org.nh.core.exception.bean.IException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname BaseController
 * @Description TODO 基础实现
 * @Date 2020/1/15 3:28 PM
 * @Created by nihui
 */
public class BaseController implements IBaseController {

    /***
     * 操作成功返回
     * @param data
     * @return
     */
    public ResponseEntity ok(Object data) {
        Result result = new Result();
        result.setData(data);
        result.setStatus(true);
        return ResponseEntity.ok(result);
    }

    /**
     * 预留扩展接口
     * @return
     */
    public ResponseEntity ok() {
        return ok(null);
    }

    /**
     * 接收到报错信息之后返回
     * @param errorMsg
     * @param data
     * @return
     */
    public ResponseEntity error(IException errorMsg, Object data) {
        return error(HttpStatus.BAD_REQUEST, errorMsg, data);
    }

    /**
     * 预留空实现
     * @param errorMsg
     * @return
     */
    public ResponseEntity error(IException errorMsg) {
        return error(HttpStatus.BAD_REQUEST, errorMsg, null);
    }

    /**
     * 错误响应
     * @param status
     * @param iResult
     * @param data
     * @return
     */
    public ResponseEntity error(HttpStatus status, IException iResult, Object data) {
        Result result = new Result();
        result.setStatus(false);
        result.setErr(iResult);
        result.setData(data);
        return ResponseEntity.status(status).body(result);
    }

    /**
     * 构建器
     *
     * 待扩展
     */
    @Data
    public static class Build {
        private transient Map<String, Object> data = new HashMap<>();
        private transient IException iResult;
        private transient boolean status = true;
        private transient HttpStatus statusCode = HttpStatus.OK;

        public ResponseEntity build(){
            Result result = new Result();
            result.setStatus(status);
            result.setData(data);
            return ResponseEntity.status(statusCode).body(result);
        }

        public Build put(String key, Object value){
            data.put(key,value);
            return this;
        }
    }

    public Build data(){
        Build build = new Build();
        return build;
    }
}
