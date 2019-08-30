package com.mglj.totorial.framework.tool.mq;


import com.mglj.totorial.framework.tool.moduling.BasicModule;
import com.mglj.totorial.framework.tool.mq.interceptor.MessageWrapper;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by zsp on 2018/9/4.
 */
public class AbstractConsumer<T> extends BasicModule implements Consumer<T> {

    protected final ConsumerFactory consumerFactory;
    protected final Set<MessageListener> messageListeners
            = new CopyOnWriteArraySet<MessageListener>();

    protected AbstractConsumer(ConsumerFactory consumerFactory, MqEvent event){
        super("MQ Consumer - " + event);
        this.consumerFactory = consumerFactory;
    }

    protected void handleMessage(T message) {
        MessageManager messageManager = consumerFactory.getMessageManager();
        MessageWrapper<T> messageWrapper = new MessageWrapper<>(message,
                consumerFactory.getSequenceGenerator().generate());
        messageManager.fireBeforeHandle(messageWrapper);
        try {
            for(MessageListener eventListener : messageListeners) {
                eventListener.handle(message);
            }
        } catch (Exception e) {
            messageManager.fireExceptionOccurredWhenHandle(messageWrapper, e);
            throw e;
        }
        messageManager.fireAfterHandle(messageWrapper);
    }

    @Override
    public void addEventListener(MessageListener listener) {
        this.messageListeners.add(listener);
    }

    @Override
    public void removeEventListener(MessageListener listener) {
        this.messageListeners.remove(listener);
    }
}
