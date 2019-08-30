package com.mglj.totorial.framework.tool.mq;


import com.mglj.totorial.framework.tool.mq.interceptor.MessageWrapper;

/**
 * Created by zsp on 2018/9/4.
 */
public abstract class AbstractProducer<T> implements Producer<T> {

    protected ProducerFactory producerFactory;

    protected AbstractProducer(ProducerFactory producerFactory, MqEvent event) {
        this.producerFactory = producerFactory;
    }

    @Override
    public void send(T message) {
        send0(message);
    }

    @Override
    public void asyncSend(T message) {
        producerFactory.getSendThreadPool().execute(
                () -> {
                    send0(message);
                });
    }

    protected abstract void sendMessage(T message);

    private void send0(T message){
        if(message == null) {
            return;
        }
        MessageManager messageManager = producerFactory.getMessageManager();
        MessageWrapper<T> messageWrapper = new MessageWrapper<>(message,
                producerFactory.getSequenceGenerator().generate());
        messageManager.fireBeforeSend(messageWrapper);
        try {
            sendMessage(message);
        } catch (Exception e) {
            messageManager.fireExceptionOccurredWhenSend(messageWrapper, e);
            throw e;
        }
        messageManager.fireAfterSend(messageWrapper);
    }

}
