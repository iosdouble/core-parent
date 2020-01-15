package org.nh.core.web.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.nh.core.exception.ParentException;
import org.nh.core.exception.ParentRuntimeException;
import org.nh.core.exception.bean.ExceptionMsg;
import org.nh.core.web.contants.ResultRespStatus;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ResultResp<T> extends AbsJsonResp {

	private static final long serialVersionUID = 8262436911538688064L;
	
	@JsonProperty("date")
	private Date date=new Date();
	/**
	 * normal
	 * error
	 * exception
	 */
	@JsonProperty("status")
	private String status=ResultRespStatus.NORMAL;
	
	@JsonProperty("exception")
	private ExceptionMsg exceptionMsg;
	
	@JsonProperty("response")
	private T response;
	
	@JsonIgnore
	public void setException(ParentException parentException){
		this.exceptionMsg=parentException.getExceptionMsg();
	}
	
	@JsonIgnore
	public void setException(ParentRuntimeException parentRuntimeException){
		this.exceptionMsg=parentRuntimeException.getExceptionMsg();
	}

}
