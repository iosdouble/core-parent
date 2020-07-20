//package org.nh.core.web.filter.cookie;
//
//import com.gome.gscm.svn.Secure;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * Created by wangshuai-ds3 on 2017/4/26.
// */
//public class CookieTool {
//
//    private CookieTool() {
//    }
//
//    /**
//     * 添加cookie
//     *
//     * @param response
//     * @param name
//     * @param value
//     * @param maxAge
//     */
//    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) throws Exception {
//        Cookie cookie = new Cookie(name, Secure.encrypt(value));
//        cookie.setPath("/");
//        if (maxAge > 0) {
//            cookie.setMaxAge(maxAge);
//        }
//        response.addCookie(cookie);
//    }
//
//    /**
//     * 删除cookie
//     *
//     * @param response
//     * @param name
//     */
//    public static void removeCookie(HttpServletResponse response, String name) {
//        Cookie uid = new Cookie(name, null);
//        uid.setPath("/");
//        uid.setMaxAge(0);
//        response.addCookie(uid);
//    }
//
//    /**
//     * 获取cookie值
//     *
//     * @param request
//     * @return
//     */
//    public static String getUid(HttpServletRequest request, String cookieName) throws Exception {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals(cookieName)) {
//                    return Secure.decrypt(cookie.getValue());
//                }
//            }
//        }
//        return null;
//    }
//}
