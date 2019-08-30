package com.mglj.totorial.framework.tool.mq;

import com.mglj.totorial.framework.tool.mq.interceptor.MessageReceiveInterceptor;
import com.mglj.totorial.framework.tool.mq.interceptor.MessageSendInterceptor;
import com.mglj.totorial.framework.tool.mq.interceptor.MessageWrapper;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by zsp on 2018/9/4.
 */
public abstract class MessageManager  {

    private final Set<MqEvent> events = ConcurrentHashMap.newKeySet();
    private final List<MessageSendInterceptor> messageSendInterceptorList
            = new CopyOnWriteArrayList<>();
    private final List<MessageReceiveInterceptor> messageReceiveInterceptorList
            = new CopyOnWriteArrayList<>();

    public boolean isRegistrated(MqEvent event) {
        return events.contains(event);
    }

    public void registrate(MqEvent event) {
        doRegistrate(event);
        events.add(event);
    }

    public void addMessageSendInterceptor(MessageSendInterceptor interceptor) {
        messageSendInterceptorList.add(interceptor);
    }

    public void removeMessageSendInterceptor(MessageSendInterceptor interceptor) {
        messageSendInterceptorList.remove(interceptor);
    }

    public List<MessageSendInterceptor> listMessageSendInterceptor() {
        return messageSendInterceptorList;
    }

    public void addMessageReceiveInterceptor(MessageReceiveInterceptor interceptor) {
        messageReceiveInterceptorList.add(interceptor);
    }

    public void removeMessageReceiveInterceptor(MessageReceiveInterceptor interceptor) {
        messageReceiveInterceptorList.remove(interceptor);
    }

    public List<MessageReceiveInterceptor> listMessageReceiveInterceptor() {
        return messageReceiveInterceptorList;
    }

    public <T> void fireBeforeSend(MessageWrapper<T> messageWrapper) {
        if(CollectionUtils.isEmpty(messageSendInterceptorList)) {
            return;
        }
        for(MessageSendInterceptor interceptor : messageSendInterceptorList) {
            interceptor.beforeSend(messageWrapper);
        }
    }

    public <T> void fireAfterSend(MessageWrapper<T> messageWrapper) {
        if(CollectionUtils.isEmpty(messageSendInterceptorList)) {
            return;
        }
        for(MessageSendInterceptor interceptor : messageSendInterceptorList) {
            interceptor.afterSend(messageWrapper);
        }
    }

    public <T> void fireExceptionOccurredWhenSend(MessageWrapper<T> messageWrapper,
                                                 Exception e) {
        if(CollectionUtils.isEmpty(messageSendInterceptorList)) {
            return;
        }
        for(MessageSendInterceptor interceptor : messageSendInterceptorList) {
            interceptor.handleException(messageWrapper, e);
        }
    }

    public <T> void fireBeforeHandle(MessageWrapper<T> messageWrapper) {
        if(CollectionUtils.isEmpty(messageReceiveInterceptorList)) {
            return;
        }
        for(MessageReceiveInterceptor interceptor : messageReceiveInterceptorList) {
            interceptor.beforeHandle(messageWrapper);
        }
    }

    public <T> void fireAfterHandle(MessageWrapper<T> messageWrapper) {
        if(CollectionUtils.isEmpty(messageReceiveInterceptorList)) {
            return;
        }
        for(MessageReceiveInterceptor interceptor : messageReceiveInterceptorList) {
            interceptor.afterHandle(messageWrapper);
        }
    }

    public <T> void fireExceptionOccurredWhenHandle(MessageWrapper<T> messageWrapper,
                                                  Exception e) {
        if(CollectionUtils.isEmpty(messageReceiveInterceptorList)) {
            return;
        }
        for(MessageReceiveInterceptor interceptor : messageReceiveInterceptorList) {
            interceptor.handleException(messageWrapper, e);
        }
    }

    protected abstract void doRegistrate(MqEvent event);

}
