package com.mglj.totorial.framework.tool.mq;

/**
 * 消息生产方
 *
 * Created by zsp on 2018/8/31.
 */
public interface Producer<T> {

    /**
     * 发送消息
     *
     * @param message
     */
    void send(T message);

    /**
     * 异步发送消息
     *
     * @param message
     */
    void asyncSend(T message);

}
