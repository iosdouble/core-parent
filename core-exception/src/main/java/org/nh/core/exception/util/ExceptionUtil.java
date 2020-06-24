package org.nh.core.exception.util;


import org.apache.commons.lang3.StringUtils;
import org.nh.core.exception.bean.ExceptionMsg;
import org.nh.core.util.json.JsonUtil;

/**
 *
 * 异常工具类
 *
 */
public class ExceptionUtil{

	/**
	 * @description 将异常字符串转换成ExceptionMsg
	 * @param exceptionMsg
	 * @return
	 */
	public static ExceptionMsg toServiceException(String exceptionMsg){
		ExceptionMsg errorMsgBean=JsonUtil.toObject(exceptionMsg, "/exception", ExceptionMsg.class);
//		ExceptionMsg errorMsgBean=JsonUtil.toObject(exceptionMsg, ExceptionMsg.class);
		return errorMsgBean;
	}

	/**
	 * @description 判断一个json字符串是否是系统异常字符串
	 * 判断的逻辑实现在下个版本会进行改进
	 * @param exceptionMsg json字符串
	 * @return
	 */
	public static boolean isServiceException(String exceptionMsg){
		if(StringUtils.isNoneBlank(exceptionMsg) && exceptionMsg.contains("error_code")){
			return true;
		}else{
			return false;
		}
	}
	
}
