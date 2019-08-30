package com.mglj.totorial.framework.tool.mq.interceptor;

/**
 * Created by zsp on 2018/9/21.
 */
public interface MessageReceiveInterceptor {

    <T> void beforeHandle(MessageWrapper<T> messageWrapper);

    <T> void afterHandle(MessageWrapper<T> messageWrapper);

    <T> void handleException(MessageWrapper<T> messageWrapper, Exception e);

}
