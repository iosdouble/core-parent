package org.nh.core.web.jersey;


import org.nh.core.web.resp.generator.IWsRsResponseGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

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
