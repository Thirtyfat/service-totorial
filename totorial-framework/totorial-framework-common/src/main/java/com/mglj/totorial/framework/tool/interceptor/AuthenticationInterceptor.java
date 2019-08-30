package com.mglj.totorial.framework.tool.interceptor;

import com.mglj.totorial.framework.common.lang.Result;
import com.mglj.totorial.framework.tool.context.RequestContextHolder;
import com.mglj.totorial.framework.tool.context.UserContext;
import com.mglj.totorial.framework.tool.security.jwt.TokenParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zsp on 2018/8/29.
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    private TokenParser tokenParser;
    public void setTokenParser(TokenParser tokenParser) {
        this.tokenParser = tokenParser;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        String token = getToken(request);
        if(!StringUtils.hasText(token)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "未找到token信息");
            return false;
        }
        Result<UserContext> result = tokenParser.verify(token);
        if(!result.wasOk()) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "无效的token信息");
            return false;
        }
        RequestContextHolder.setUserContext(result.getResult());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
    }

    private String getToken(HttpServletRequest request) {
        return request.getHeader("token");
    }

}
