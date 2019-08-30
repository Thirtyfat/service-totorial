package com.mglj.totorial.framework.tool.mq.interceptor;

import com.mglj.totorial.framework.tool.logging.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zsp on 2018/9/21.
 */
public class TraceMessageReceiveInterceptor implements MessageReceiveInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(TraceMessageReceiveInterceptor.class);

    @Override
    public <T> void beforeHandle(MessageWrapper<T> messageWrapper) {
        messageWrapper.addProperty("millis", System.currentTimeMillis());
        StringBuilder builder = new StringBuilder();
        builder.append("rabbit C> ")
                .append(messageWrapper.getSequence())
                .append(" -message ")
                .append(messageWrapper.getMessage());
        LogUtils.info(logger, builder.toString());
    }

    @Override
    public <T> void afterHandle(MessageWrapper<T> messageWrapper) {
        StringBuilder builder = new StringBuilder();
        builder.append("rabbit C> ")
                .append(messageWrapper.getSequence())
                .append(" -cost ")
                .append(System.currentTimeMillis() - (Long)messageWrapper.getProperty("millis"));
        LogUtils.info(logger, builder.toString());
    }

    @Override
    public <T> void handleException(MessageWrapper<T> messageWrapper, Exception e) {
        StringBuilder builder = new StringBuilder();
        builder.append("rabbit C> ")
                .append(messageWrapper.getSequence())
                .append(" -cost ")
                .append(System.currentTimeMillis() - (Long)messageWrapper.getProperty("millis"))
                .append(" -exchanger ")
                .append(e.getMessage());
        LogUtils.error(logger, builder.toString());
    }
}
