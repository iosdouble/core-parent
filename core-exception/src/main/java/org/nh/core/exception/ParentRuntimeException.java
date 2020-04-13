package org.nh.core.exception;

import org.nh.core.exception.bean.ExceptionMsg;
import org.nh.core.util.json.JsonUtil;


/**
 * @class: ParentRuntimeException
 * @description: TODO 框架级别的exception父类
 * @date 2020/1/15 3:38 PM
**/
public abstract class ParentRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 713616801491210431L;
	
	private ExceptionMsg exceptionMsg=new ExceptionMsg();
	
	
	public ParentRuntimeException(ExceptionMsg exceptionMsg) {
		super();
		this.exceptionMsg = exceptionMsg;
	}
	
	public ParentRuntimeException(String errorCode ,String errorMsg) {
//		exceptionMsg.setErrorId(ExceptionIdUtil.getId());
		exceptionMsg.setErrorCode(errorCode);
		exceptionMsg.setErrorMsg(errorMsg);
	}
	
	public ExceptionMsg getExceptionMsg(){
		return this.exceptionMsg;
	}
	
	public void setErrorId(String errorId) {
		this.exceptionMsg.setErrorId(errorId);
	}
	
	public void setErrorCode(String projectErrorCode) {
		String errorCode=this.exceptionMsg.getErrorCode();
		errorCode = projectErrorCode+"-"+errorCode;
		this.exceptionMsg.setErrorCode(errorCode);
	}
	
	/**
	 *
	 * 将ParentException中的信息json化
	 * @return
	 */
	private String toJson(){
//		if(StringUtils.isBlank(exceptionMsg.getErrorId())){
//			exceptionMsg.setErrorId(ExceptionIdUtil.getId());
//		}
		String responseStr=JsonUtil.toJson(exceptionMsg);
		return responseStr;
	}
	
	/**
	 * 重写Exception.getMessage()，返回规定的json格式异常
	 */
	@Override
	public String getMessage() {
		return toJson();
	}

}
