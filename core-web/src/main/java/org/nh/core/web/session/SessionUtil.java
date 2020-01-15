package org.nh.core.web.session;

import javax.servlet.http.HttpSession;

public class SessionUtil {
	
	@SuppressWarnings("unchecked")
	public static <T> T getObject(HttpSession httpSession,String attrName, Class<T> clazz) {
		Object object = httpSession.getAttribute(attrName);
		if(object!=null) {
			return (T)object;
		}else {
			return null;
		}
	}
}
