package org.nh.core.web.jersey;

import org.nh.core.web.resp.generator.IWsRsResponseGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Iterator;

/**
 *
 * Jersey异常统一来接处理
 *
 */
public class JerseyValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
	
	@Autowired(required=false)
	protected IWsRsResponseGenerator wsrsResponseGenerator;

	@Override
	public Response toResponse(ConstraintViolationException constraintViolationException) {
		Status status = Status.OK;
		Iterator<ConstraintViolation<?>> iterator = constraintViolationException.getConstraintViolations().iterator();
		ConstraintViolation<?> constraintViolation = iterator.next();
		String errorMsg = constraintViolation.getMessage();
		Exception exception = new Exception(errorMsg);
		Response response = this.wsrsResponseGenerator.doResponse(status, exception, null);
		return response;
	}

}
