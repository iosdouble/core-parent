package org.nh.core.web.resp.util;


import org.nh.core.exception.ParentException;
import org.nh.core.exception.ParentRuntimeException;
import org.nh.core.exception.bean.ExceptionMsg;
import org.nh.core.exception.util.ExceptionIdUtil;
import org.nh.core.web.contants.ResultRespStatus;
import org.nh.core.web.resp.ResultResp;

/**
 * @class: ResultRespUtil
 * @description: TODO 处理response并返回ResultResp的工具类
 * @date 2020/1/15 3:52 PM
**/
public class ResultRespUtil {
	
	
	public static <T> ResultResp<T> doResultResp(Throwable throwable, T response){
		ResultResp<T> resultResp=new ResultResp<T>();
		if(throwable!=null) {
			if(ParentException.class.isAssignableFrom(throwable.getClass())) {
				ParentException parentException = (ParentException) throwable;
				resultResp.setStatus(ResultRespStatus.EXCEPTION);
				resultResp.setException(parentException);
			}else if(ParentRuntimeException.class.isAssignableFrom(throwable.getClass())) {
				ParentRuntimeException parentRuntimeException = (ParentRuntimeException) throwable;
				resultResp.setStatus(ResultRespStatus.EXCEPTION);
				resultResp.setException(parentRuntimeException);
			}else {
				resultResp.setStatus(ResultRespStatus.ERROR);

				ExceptionMsg exceptionMsg=new ExceptionMsg();
				exceptionMsg.setErrorCode("0");
				exceptionMsg.setErrorId(ExceptionIdUtil.getId());
				exceptionMsg.setErrorMsg(throwable.getMessage());
				resultResp.setExceptionMsg(exceptionMsg);
			}
		}
		resultResp.setResponse(response);
		return resultResp;
	}
	
}
