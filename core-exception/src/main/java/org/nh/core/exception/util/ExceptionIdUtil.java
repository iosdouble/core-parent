package org.nh.core.exception.util;

import java.util.UUID;

/**
 *
 * 异常的ErrorId处理工具
 * @deprecated 被{@code IExceptionIdGenerator}取代
 * {@code IExceptionIdGenerator}为一个异常ID生成的标准接口，其有多种实现
 * 例如：
 * {@code SnowflakeExceptionIdGeneratorImpl}
 * {@code UUIDExceptionIdGeneratorImpl}
 *
 */
public class ExceptionIdUtil {
	
	/**
	 * 
	 * @author 王云龙
	 * @date 2017年12月25日 下午12:14:38
	 * @version 1.0
	 * @description 获取Exception的id
	 *
	 * @return
	 */
	public static String getId(){
		String id=UUID.randomUUID().toString();
		return id;
	}
}
