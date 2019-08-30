package com.mglj.totorial.framework.tool.mq.interceptor;

import com.mglj.totorial.framework.tool.logging.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zsp on 2018/9/4.
 */
public class TraceMessageSendInterceptor implements MessageSendInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(TraceMessageSendInterceptor.class);

    @Override
    public <T> void beforeSend(MessageWrapper<T> messageWrapper) {
        messageWrapper.addProperty("millis", System.currentTimeMillis());
        StringBuilder builder = new StringBuilder();
        builder.append("rabbit P> ")
                .append(messageWrapper.getSequence())
                .append(" -message ")
                .append(messageWrapper.getMessage());
        LogUtils.info(logger, builder.toString());
    }

    @Override
    public <T> void afterSend(MessageWrapper<T> messageWrapper) {
        StringBuilder builder = new StringBuilder();
        builder.append("rabbit P> ")
                .append(messageWrapper.getSequence())
                .append(" -cost ")
                .append(System.currentTimeMillis() - (Long)messageWrapper.getProperty("millis"));
        LogUtils.info(logger, builder.toString());
    }

    @Override
    public <T> void handleException(MessageWrapper<T> messageWrapper, Exception e) {
        StringBuilder builder = new StringBuilder();
        builder.append("rabbit P> ")
                .append(messageWrapper.getSequence())
                .append(" -cost ")
                .append(System.currentTimeMillis() - (Long)messageWrapper.getProperty("millis"))
                .append(" -exchanger ")
                .append(e.getMessage());
        LogUtils.error(logger, builder.toString());
    }
}
