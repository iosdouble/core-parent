package org.nh.core.web.controller;

import lombok.Data;
import org.nh.core.exception.bean.IException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname BaseController
 * @Description TODO
 * @Date 2020/1/15 3:28 PM
 * @Created by nihui
 */
public class BaseController {

    public ResponseEntity ok(Object data) {
        Result result = new Result();
        result.setData(data);
        result.setStatus(true);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity ok() {
        return ok(null);
    }

    public ResponseEntity error(IException errorMsg, Object data) {
        return error(HttpStatus.BAD_REQUEST, errorMsg, data);
    }

    public ResponseEntity error(IException errorMsg) {
        return error(HttpStatus.BAD_REQUEST, errorMsg, null);
    }

    public ResponseEntity error(HttpStatus status, IException iResult, Object data) {
        Result result = new Result();
        result.setStatus(false);
        result.setErr(iResult);
        result.setData(data);
        return ResponseEntity.status(status).body(result);
    }

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
