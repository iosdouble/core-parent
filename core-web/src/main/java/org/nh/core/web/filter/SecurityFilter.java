//package org.nh.core.web.filter;
//
//import com.alibaba.fastjson.JSONObject;
//import com.gome.gscm.context.Context;
//import com.gome.gscm.cookie.CookieTool;
//import com.gome.gscm.security.IpUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * Created by root on 4/14/17.
// */
//@WebFilter(filterName = "SecurityFilter")
//public class SecurityFilter implements Filter {
//
//	public void destroy() {
//	}
//
//	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
//		HttpServletRequest r = (HttpServletRequest) req;
//		HttpServletResponse rp = (HttpServletResponse) resp;
//		String requestURI = r.getRequestURI();
//		String callback = r.getParameter("callback");
//		String value = null;
//		try {
//			value = CookieTool.getUid(r, "username");
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//        if (!requestURI.contains("login.do") && !requestURI.contains("logout.do") && !requestURI.contains("/path/check.do")) {
//            if (StringUtils.isEmpty(value)) {
//				// 封装返回值，跳转到登录页面
//				JSONObject rpJO = new JSONObject();
//				rpJO.put("flag", "N");
//				rpJO.put("islogin", "0");
//				rp.getWriter().write(callback + "(" + rpJO.toString() + ")");
//			} else {
//				Context.set("username", value);
//				Context.set("ipAddr", IpUtils.getRemoteIp(r));
//				chain.doFilter(req, resp);
//			}
//		} else {
//			chain.doFilter(req, resp);
//		}
//	}
//
//	public void init(FilterConfig config) throws ServletException {
//
//	}
//
//}
