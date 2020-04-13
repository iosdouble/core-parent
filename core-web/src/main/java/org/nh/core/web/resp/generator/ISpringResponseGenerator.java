package org.nh.core.web.resp.generator;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @class: ISpringResponseGenerator
 * @description: TODO 基于HttpServletResponse的PrintWriter进行输出控制标准接口
 * @date 2020/1/15 3:46 PM
**/
public interface ISpringResponseGenerator {
	
	/**
	 * 将HTTP状态及字符串信息写入HttpServletResponse对象
	 * @param httpServletResponse {@code HttpServletResponse}
	 * @param status {@code HttpServletResponse}
	 * @param msg
	 * @throws IOException 
	 */
	public void doResponse(HttpServletResponse httpServletResponse, int status, String msg) throws IOException;

	/**
	 *
	 * 将HTTP状态、contentType及字符串信息写入HttpServletResponse对象
	 * @param httpServletResponse {@code HttpServletResponse}
	 * @param status {@code HttpServletResponse}
	 * @param contentType {@code MediaType}
	 * @param msg
	 * @throws IOException
	 */
	public void doResponse(HttpServletResponse httpServletResponse, int status, String contentType, String msg) throws IOException;
	
}
