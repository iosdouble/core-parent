package org.nh.core.exception.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 异常信息处理类，增加了@ApiModel注解，方便在Swagger2工具中使用展示接口信息
 *
 * 主要包含了 三个属性
 *  errorId 错误ID，可以使用UUID来定位，在很多场景中使用这个ID可以精确的定位到异常信息
 *  errorCode  错误码和错误信息是通过一个 Enum对象存储，这里借鉴了 例如 404 500 505 等这样的HTTP协议错误码来编写
 *  errorMsg 错误信息 ，实现错误信息的精准描述
 */
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
