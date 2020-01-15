package org.nh.core.exception.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@ApiModel(description = "异常处理信息类")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ExceptionMsg implements Serializable {


	@ApiModelProperty(value = "错误 ID",required=true, example = "UUID",position = 1)
	@JsonProperty("error_id")
	private String errorId;
	
	@ApiModelProperty(value = "错误 code",required=true, example = "100110121",position = 2)
	@JsonProperty("error_code")
	private String errorCode;
	
	@ApiModelProperty(value = "错误信息",required=true, example = "在注册时候用户名不能为空",position = 3)
	@JsonProperty("error_msg")
	private String errorMsg;

}
