package org.nh.core.web.resp.util;

import org.apache.commons.lang3.StringUtils;
import org.nh.core.exception.ParentException;
import org.nh.core.exception.bean.ExceptionMsg;
import org.nh.core.exception.util.ExceptionIdUtil;
import org.nh.core.util.string.CharsetContants;
import org.nh.core.web.contants.ResultRespStatus;
import org.nh.core.web.resp.ResultResp;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @class: ResponseUtil
 * @description: TODO 处理response的工具类
 * @date 2020/1/15 3:51 PM
**/
public class ResponseUtil {
	/**
	 *
	 * 将HTTP状态及字符串信息写入HttpServletResponse对象
	 * 
	 * @param httpServletResponse HttpServletResponse
	 * @param status status
	 * @param msg 字符串信息
	 * @throws IOException 
	 */
	public static void doResponse(HttpServletResponse httpServletResponse,int status,String msg) throws IOException{
		doResponse(httpServletResponse, status, null, msg);
	}
	
	/**
	 * 
	 * @param httpServletResponse
	 * @param status
	 * @param contentType
	 * @param msg
	 * @throws IOException
	 */
	public static void doResponse(HttpServletResponse httpServletResponse,int status,String contentType, String msg) throws IOException{
		httpServletResponse.setStatus(status);
		if(!StringUtils.isBlank(contentType)) {
			httpServletResponse.setContentType(contentType);
		}
		httpServletResponse.setCharacterEncoding(CharsetContants.UTF8);
		PrintWriter printWriter=httpServletResponse.getWriter();
		printWriter.write(msg);
		printWriter.flush();  
		printWriter.close();  
		return;
	}
	
	/**
	 * 基于Rest标准的相应
	 *
	 * @param status
	 * @param parentException
	 * @param response
	 * @throws IOException
	 */
	public static <T> Response doResponse(Status status, ParentException parentException, T response){
		ResultResp<T> resultResp=doResultResp(parentException, response);
		return Response.status(status).entity(resultResp.toString()).build();
	}
	
	/**
	 *
	 * 根据异常和数据组装成返回结果
	 *
	 * @param parentException
	 * @param response
	 * @return
	 */
	public static <T> ResultResp<T> doResultResp(ParentException parentException,T response){
		ResultResp<T> resultResp=new ResultResp<T>();
		if(parentException!=null){
			resultResp.setStatus(ResultRespStatus.EXCEPTION);
			resultResp.setException(parentException);
		}
		resultResp.setResponse(response);
		return resultResp;
	}
	
	/**
	 *
	 * 根据服务器异常和是否包含堆栈信息，服务器异常包装后返回，errorCode为0
	 * 
	 * @param error
	 * @param includeStackTrace
	 * @return
	 */
	public static <T> ResultResp<T> doResultResp(Throwable error, boolean includeStackTrace){
		ResultResp<T> resultResp=new ResultResp<T>();
		resultResp.setStatus(ResultRespStatus.ERROR);
		
		ExceptionMsg exceptionMsg=new ExceptionMsg();
		exceptionMsg.setErrorCode("0");
		exceptionMsg.setErrorId(ExceptionIdUtil.getId());
		exceptionMsg.setErrorMsg(error.getMessage());
		resultResp.setExceptionMsg(exceptionMsg);
		return resultResp;
	}
	
	
}
