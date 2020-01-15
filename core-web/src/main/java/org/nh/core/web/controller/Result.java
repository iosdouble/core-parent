package org.nh.core.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nh.core.exception.bean.IException;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result {
    /** 状态true or false */
    private Boolean status;
    /** 错误信息*/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private IException err;
    /** 返回的业务数据*/
    private Object data;
}
