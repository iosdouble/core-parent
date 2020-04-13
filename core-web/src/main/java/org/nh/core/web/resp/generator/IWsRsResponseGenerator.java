package org.nh.core.web.resp.generator;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * @class: IWsRsResponseGenerator
 * @description: TODO 基于ws.rs的Response进行输出控制标准接口
 * @date 2020/1/15 3:47 PM
**/
public interface IWsRsResponseGenerator {
	
	/**
	 * 将自定义的json返回结果使用ws.rs标准进行返回
	 * @param status
	 * @param response
	 * @return
	 */
	public <T> Response doResponse(Status status, Throwable throwable, T response);
	
}
