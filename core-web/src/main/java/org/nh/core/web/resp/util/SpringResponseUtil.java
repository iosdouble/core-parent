package org.nh.core.web.resp.util;

import org.apache.commons.lang3.StringUtils;
import org.nh.core.util.string.CharsetContants;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @class: SpringResponseUtil
 * @description: TODO Spring的Response处理的util
 * @date 2020/1/15 3:53 PM
**/
public class SpringResponseUtil {
	/**
	 *
	 * 将HTTP状态及字符串信息写入HttpServletResponse对象
	 * @param httpServletResponse HttpServletResponse
	 * @param status status
	 * @param msg 字符串信息
	 * @throws IOException 
	 */
	public static void doResponse(HttpServletResponse httpServletResponse,int status,String msg) throws IOException{
		doResponse(httpServletResponse, status, null, msg);
	}
	
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
}
