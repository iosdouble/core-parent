package org.nh.core.web.req.util;

import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @class: AjaxUtil
 * @description: TODO ajax的工具类
 * @date 2020/1/15 3:42 PM
**/
public class AjaxUtil {
	/**
	 * 判断请求是否是Ajax
	 * @param request
	 * @return
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		String header0 = request.getHeader("X-Requested-With");
		if("XMLHttpRequest".equals(header0)) {
			return true;
		}
		String header1 = request.getHeader("accept");
		if(header1!=null && header1.contains("application/json")) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isAjaxRequest(WebRequest request) {
		String header0 = request.getHeader("X-Requested-With");
		if("XMLHttpRequest".equals(header0)) {
			return true;
		}
		String header1 = request.getHeader("accept");
		if(header1!=null && header1.contains("application/json")) {
			return true;
		}
		
		return false;
	}
}
