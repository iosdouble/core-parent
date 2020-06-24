package org.nh.core.web.jersey;


import org.nh.core.web.resp.generator.IWsRsResponseGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;


/**
 * 1. jersey同样提供DI，是由glassfish hk2实现，也就是说，如果想单独使用
 * jersey一套，需要另外学习Bean容器；
 *
 * 2. MVC出发点即是WEB，但jersey出发点确实RESTFull，体现点在与接口的设计方面，
 * 如MVC返回复杂结构需要使用ModelAndView,而jersey仅仅需要返回一个流或者文件句柄；
 *
 * 3. jersey提供一种子资源的概念，这也是RESTFull中提倡所有url都是资源；
 *
 * 4. jersey直接提供application.wadl资源url说明；
 *
 * 5. MVC提供Session等状态管理，jersey没有，这个源自RESTFull设计无状态化；
 *
 * 6. Response方法支持更好返回结果，方便的返回Status，包括200，303，401，403；
 *
 * 7. 提供超级特别方便的方式访问RESTFull;
 */

/**
 *
 * Jersey异常统一来接处理
 *
 */
public class JerseyCatExceptionMapper implements ExceptionMapper<Throwable> {
	
	@Autowired(required=false)
	protected IWsRsResponseGenerator wsrsResponseGenerator;

	@Override
	public Response toResponse(Throwable throwable) {
		Status status = Status.OK;
		Response response = this.wsrsResponseGenerator.doResponse(status, throwable, null);
		return response;
	}

}
