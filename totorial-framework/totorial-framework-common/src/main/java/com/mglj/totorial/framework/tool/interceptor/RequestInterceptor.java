package com.mglj.totorial.framework.tool.interceptor;

import com.mglj.totorial.framework.common.util.NetUtils;
import com.mglj.totorial.framework.tool.context.RequestContextHolder;
import com.mglj.totorial.framework.tool.logging.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.UnknownHostException;

/**
 * Created by zsp on 2018/8/30.
 */
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    private final static String KEY_APP_NAME = "appName";
    private final static String KEY_IP = "ip";

    private String applicationName;
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    private String ip;

    public RequestInterceptor() {
        try {
            ip = NetUtils.getHostAddress();
        } catch (UnknownHostException e) {
            LogUtils.error(logger, "获取主机IP异常", e);
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        RequestContextHolder.clear();

        MDC.put(KEY_APP_NAME, applicationName);
        MDC.put(KEY_IP, ip);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
        MDC.remove(KEY_APP_NAME);
        MDC.remove(KEY_IP);
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                @Nullable Exception ex) throws Exception {
        RequestContextHolder.clear();
    }
}
