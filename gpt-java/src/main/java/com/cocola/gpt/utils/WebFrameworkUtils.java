package com.cocola.gpt.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Author: yangshiyuan
 * @Date: 2022/9/14
 * @Description: 专属于 web 包的工具类
 */
public class WebFrameworkUtils {

    private static final String PC_TOKEN = "COSBIKE_ADMIN_LOGIN";
    private static final String COSBIKE_ADMIN_NICKNAME = " COSBIKE_ADMIN_NICKNAME";

    private WebFrameworkUtils() {
    }

    public static void setLoginUserId(ServletRequest request, Long userId) {
        request.setAttribute(PC_TOKEN, userId);
    }

    public static void setLoginUserType(ServletRequest request, Integer userType) {
        request.setAttribute(COSBIKE_ADMIN_NICKNAME, userType);
    }


    /**
     * 获得当前用户的编号，从请求中
     *
     * @param request 请求
     * @return 用户编号
     */
    public static String getLoginUserId(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
        if (null == request.getAttribute(PC_TOKEN)) {
            return null;
        }
        return String.valueOf(request.getAttribute(PC_TOKEN));
    }


    public static String getLoginUserId() {
        HttpServletRequest request = getRequest();
        return getLoginUserId(request);
    }

    public static String getLoginToken() {
        HttpServletRequest request = getRequest();
        return getLoginToken(request);
    }

    /**
     * 获得当前用户的编号，从请求中
     *
     * @param request 请求
     * @return 用户编号
     */
    public static String getLoginToken(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
        if (null == request.getCookies()) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (Objects.equals(cookie.getName(), PC_TOKEN)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return null;
        }
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        return servletRequestAttributes.getRequest();
    }

    public static String getRequestUrl() {
        HttpServletRequest request = getRequest();
        return getRequestUrl(request);
    }

    public static String getRequestUrl(HttpServletRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }

        if (Objects.isNull(request.getRequestURL())) {
            return null;
        }

        return String.valueOf(request.getRequestURL());
    }

    /**
     * 从cookie获取TOKEN
     *
     * @return
     */
    public static String getTokenByCookies() {
        HttpServletRequest request = getRequest();
        return getTokenByCookies(request);
    }

    /**
     * 从cookie获取TOKEN
     *
     * @return
     */
    public static String getTokenByCookies(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
        if (null == request.getCookies()) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (Objects.equals(cookie.getName(), PC_TOKEN)) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
