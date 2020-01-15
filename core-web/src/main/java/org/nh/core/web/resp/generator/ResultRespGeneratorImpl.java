package org.nh.core.web.resp.generator;


import org.nh.core.exception.ParentException;
import org.nh.core.exception.ParentRuntimeException;
import org.nh.core.exception.bean.ExceptionMsg;
import org.nh.core.exception.code.IExceptionCodeGenerator;
import org.nh.core.exception.id.IExceptionIdGenerator;
import org.nh.core.web.contants.ResultRespStatus;
import org.nh.core.web.resp.ResultResp;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @class: ResultRespGeneratorImpl
 * @description: TODO 自定义的Json返回结果的实现标准接口的实现类
 * @date 2020/1/15 3:48 PM
**/
public class ResultRespGeneratorImpl implements IResultRespGenerator {

	@Autowired
	private IExceptionIdGenerator exceptionIdGenerator;
	@Autowired
	private IExceptionCodeGenerator exceptionCodeGenerator;
	
	@Override
	public <T> ResultResp<T> doResultResp(Throwable throwable, T response) {
		ResultResp<T> resultResp=new ResultResp<T>();
		if(throwable!=null) {
			if(ParentException.class.isAssignableFrom(throwable.getClass())) {
				ParentException parentException = (ParentException) throwable;
				parentException.setErrorId(exceptionIdGenerator.getId());
				parentException.setErrorCode(exceptionCodeGenerator.getProjectExceptionCode());
				resultResp.setStatus(ResultRespStatus.EXCEPTION);
				resultResp.setException(parentException);
			}else if(ParentRuntimeException.class.isAssignableFrom(throwable.getClass())) {
				ParentRuntimeException parentRuntimeException = (ParentRuntimeException) throwable;
				parentRuntimeException.setErrorId(exceptionIdGenerator.getId());
				parentRuntimeException.setErrorCode(exceptionCodeGenerator.getProjectExceptionCode());
				resultResp.setStatus(ResultRespStatus.EXCEPTION);
				resultResp.setException(parentRuntimeException);
			}else {
				resultResp.setStatus(ResultRespStatus.ERROR);
				ExceptionMsg exceptionMsg=new ExceptionMsg();
				exceptionMsg.setErrorCode(exceptionCodeGenerator.getProjectExceptionCode()+"-0");
				exceptionMsg.setErrorId(exceptionIdGenerator.getId());
				exceptionMsg.setErrorMsg(throwable.getMessage());
				resultResp.setExceptionMsg(exceptionMsg);
				throwable.printStackTrace();
			}
		}
		resultResp.setResponse(response);
		return resultResp;
	}
	
	@Override
	public <T> ResultResp<T> doResultResp(Throwable throwable) {
		ResultResp<T> resultResp=doResultResp(throwable, null);
		return resultResp;
	}

	@Override
	public <T> ResultResp<T> doResultResp(T response) {
		ResultResp<T> resultResp=doResultResp(null, response);
		return resultResp;
	}
	
}
