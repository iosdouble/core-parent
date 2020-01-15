package org.nh.core.web.resp.generator;

import org.apache.commons.lang3.StringUtils;
import org.nh.core.util.string.CharsetContants;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @class: SpringResponseGeneratorImpl
 * @description: TODO 基于HttpServletResponse的PrintWriter进行输出控制标准接口的标准实现
 * @date 2020/1/15 3:48 PM
**/
public class SpringResponseGeneratorImpl implements ISpringResponseGenerator {

	@Override
	public void doResponse(HttpServletResponse httpServletResponse, int status, String msg) throws IOException {
		doResponse(httpServletResponse, status, null, msg);
	}

	@Override
	public void doResponse(HttpServletResponse httpServletResponse, int status, String contentType, String msg) throws IOException {
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
