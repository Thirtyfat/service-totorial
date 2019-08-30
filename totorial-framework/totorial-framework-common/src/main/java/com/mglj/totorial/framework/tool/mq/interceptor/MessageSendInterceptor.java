package com.mglj.totorial.framework.tool.mq.interceptor;

/**
 * Created by zsp on 2018/9/4.
 */
public interface MessageSendInterceptor {

    <T> void beforeSend(MessageWrapper<T> messageWrapper);

    <T> void afterSend(MessageWrapper<T> messageWrapper);

    <T> void handleException(MessageWrapper<T> messageWrapper, Exception e);

}
