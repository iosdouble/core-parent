package org.nh.core.web.resp.util;

import org.nh.core.exception.ParentException;
import org.nh.core.web.resp.ResultResp;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

/**
 *
 * ws.rs的response相应工具
 * @category {@code IWsRsResponseGenerator}
 *
 */
public class WsRsResponseUtil {
	/**
	 *
	 * 根据httpStatus、自定义异常和结果返回结果
	 *
	 * @param status {@code HttpServletResponse}
	 * @param parentException {@code ParentException}
	 * @param response
	 * @throws IOException
	 */
	public static <T> Response doResultResp(Status status, ParentException parentException, T response){
		ResultResp<T> resultResp=ResultRespUtil.doResultResp(parentException, response);
		return Response.status(status).entity(resultResp.toString()).build();
	}
}
