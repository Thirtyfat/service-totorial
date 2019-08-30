package com.mglj.totorial.framework.tool.context;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by zsp on 2018/9/17.
 */
public class ServletContextHolder {

    public static HttpServletRequest getRequest() {
        RequestAttributes ra = org.springframework.web.context.request.RequestContextHolder
                .getRequestAttributes();
        if (ra == null) {
            return null;
        }

        HttpServletRequest request = ((ServletRequestAttributes) ra)
                .getRequest();

        return request;
    }

    public static String getParameter(String name) {
        HttpServletRequest request = getRequest();
        if (request != null) {
            return request.getParameter(name);
        }

        return null;
    }

    public static String getHeader(String name) {
        HttpServletRequest request = getRequest();
        if (request != null) {
            return request.getHeader(name);
        }

        return null;
    }

    public static String getBearer() {
        String authorization = getHeader("Authorization");
        if (authorization == null || authorization.length() <= 7) {
            return null;
        }
        return authorization.substring(7);
    }

    public static Map<String, String> getHeaders() {
        Map<String, String> headerMap = new HashMap<>();
        HttpServletRequest request = getRequest();
        if (request != null) {
            Enumeration<String> names = request.getHeaderNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                headerMap.put(name, request.getHeader(name));
            }

        }

        return headerMap;
    }

    public static String getRemoteAddr() {
        HttpServletRequest request = getRequest();
        if (request != null) {
            String ipFromClient = getHeader("X-Forwarded-For");

            if(!StringUtils.hasText(ipFromClient)) {
                return request.getRemoteAddr();
            }

            int index = ipFromClient.indexOf(",");
            if (index > -1){
                ipFromClient=ipFromClient.split(",")[0];
            }
            if(!StringUtils.hasText(ipFromClient)){
                return request.getRemoteAddr();
            }else{
                return ipFromClient;
            }
        }

        return null;
    }

    public static String getUrl() {
        HttpServletRequest request = getRequest();

        if (request != null) {
            String orgUrl = request.getHeader("request_url");
            if (!StringUtils.isEmpty(orgUrl)) {
                // 请求头还有原始URL信息
                return orgUrl;
            }

            StringBuffer url = request.getRequestURL();
            if (!StringUtils.isEmpty(request.getQueryString())) {
                url.append('?');
                url.append(request.getQueryString());
            }
            return url.toString();
        }

        return null;
    }

    public static String getReferUrl() {
        HttpServletRequest request = getRequest();

        if (request != null) {
            String referUrl = request.getHeader("Referer");
            return referUrl;
        }

        return null;
    }

    public static String getUserAgent() {
        return getHeader("User-Agent");
    }

    public static String getCookie(String name) {
        HttpServletRequest request = getRequest();

        Cookie[] cookies = request.getCookies();
        String value = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (!StringUtils.isEmpty(cookie.getName())
                        && Objects.equals(name, cookie.getName())) {
                    value = cookie.getValue();
                    break;
                }
            }
        }

        return value;
    }

    public static Map<String, Object> getParamMap() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        if (req == null) {
            return paramMap;
        }

        Enumeration<String> names = req.getParameterNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();
            Object value = req.getParameter(name);

            paramMap.put(name, value);
        }

        return paramMap;
    }

}
