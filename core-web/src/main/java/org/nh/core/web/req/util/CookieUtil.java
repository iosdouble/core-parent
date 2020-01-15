package org.nh.core.web.req.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @class: CookieUtil
 * @description: TODO 处理cookie的工具
 * @date 2020/1/15 3:42 PM
**/
public class CookieUtil {
	
	/**
	 *
	 * 获取cookie的键值对，键为cookie的名称，值为cookie对象
	 * @param request HttpServletRequest
	 * @return
	 */
	private  static Map<String,Cookie> ReadCookieMap(HttpServletRequest request){  
        Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
        Cookie[] cookies = request.getCookies();
        if(null!=cookies){
            for(Cookie cookie : cookies){
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }
	
	/**
	 *
	 * 读取指定cookie对象
	 * @param request HttpServletRequest
	 * @param name cookie的名称，即cookie.getName()获取到的名称
	 * @return
	 */
	public  static Cookie getCookieByName(HttpServletRequest request,String name){
        Map<String,Cookie> cookieMap = ReadCookieMap(request);
        if(cookieMap.containsKey(name)){
            Cookie cookie = cookieMap.get(name);
            return cookie;
        }else{
            return null;
        }   
    }
	
	/**
	 * 从cookie中获取对应name的value
	 * 如果cookie不存在，则返回null 
	 * @param request HttpServletRequest
	 * @param name cookie的名称，即cookie.getName()获取到的名称
	 * @return
	 */
	public static String getCookieValueByName(HttpServletRequest request,String name){
		Cookie cookie=getCookieByName(request, name);
		if(cookie==null){
			return null;
		}else{
			return cookie.getValue();
		}
	}
	
	/**
	 *
	 * 添加新的cookie
	 * @param response HttpServletResponse
	 * @param name cookie的名称，即cookie.getName()获取到的名称
	 * @param value cookie中保存的值，会自动去除字符串两边的空格，方该字符串为空或者null，则会自动设置该cookie失效
	 * @param maxAge cookie的声明周期，0表示cookie马上过期，-1表示会话级cookie关闭浏览器失效，60*60表示1小时过期等
	 * @param domain cookie所在的域
	 * 如果传递null，则默认为请求的地址，否则为设置的地址
	 * 如果请求URL为www.cat.org/test/test，那么domain默认为www.cat.org
	 * 如果请求URL为a.cat.org/test/test，那么domain默认为a.cat.org，对于b.cat.org属于跨域
	 * 如果需要解决跨域的问题，则需要设置domain为cat.org
	 * @param path cookie所在的目录
	 * 如果为null，则默认为/
	 * 子路径下的cookie能够被父路径的请求所访问
	 * 但子路径的请求不能访问父路径的cookie
	 * @param isHttpOnly 如果设置为true，JavaScript不能读取和修改cookie
	 * @param isSecure 如果设置为true，表示创建的Cookie会被以安全的形式向服务器传输，只能在HTTPS连接中被传递到服务器端
	 */
	public static boolean addCookie(
			HttpServletResponse response,
			String name,
			String value,
			int maxAge,
			String domain,
			String path,
			boolean isHttpOnly,
			boolean isSecure){
		if(StringUtils.isBlank(name)){
			return false;
		}
		name=name.trim();
		
		if(StringUtils.isNotBlank(value)){
			value=value.trim();
		}
		
		Cookie cookie = new Cookie(name, value);
		
		if(StringUtils.isBlank(value)){
			 cookie.setMaxAge(0);
		}else{
			cookie.setMaxAge(maxAge);
		}
		
		if(StringUtils.isNotBlank(domain)){
			cookie.setDomain(domain);
		}
		
		if(StringUtils.isBlank(path)){
			cookie.setPath("/");
		}else{
			cookie.setPath(path);
		}
        
        cookie.setHttpOnly(isHttpOnly);
        cookie.setSecure(isSecure);
        
        response.addCookie(cookie);
        return true;
	}
	
	/**
	 * 添加新的cookie
	 * doamin为当前请求的域
	 * path为/
	 * isHttpOnly为false
	 * isSecure为false
	 * @param response HttpServletResponse
	 * @param name cookie
	 * @param value value
	 * @param maxAge 最大有效时间
	 * return
	 */
	public static boolean addCookie(
			HttpServletResponse response,
			String name,
			String value,
			int maxAge){
		
		HttpServletResponse response1=response;
		String name1=name;
		String value1=value;
		int maxAge1=maxAge;
		String domain1=null;
		String path1=null;
		boolean isHttpOnly1=false;
		boolean isSecure1=false;
		
        return addCookie(response1, name1, value1, maxAge1, domain1,path1, isHttpOnly1, isSecure1);
    }
	
	
	
	/**
	 *
	 * 编辑指定的cookie
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param name name
	 * @param value value
	 * @param maxAge maxAge
	 */
    public static void  editCookie(
    		HttpServletRequest request,
    		HttpServletResponse response,
    		String name,
    		String value,
    		int maxAge){
    	
    	Cookie cookie=getCookieByName(request, name);
    	cookie.setValue(value);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
    
    /**
     *
	 * 删除指定的cookie
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param name name
     */
    public static void  delCookie(
    		HttpServletRequest request,
    		HttpServletResponse response,
    		String name){
    	
    	Cookie cookie=getCookieByName(request, name);
    	cookie.setMaxAge(0);
    	response.addCookie(cookie);
    }
}
