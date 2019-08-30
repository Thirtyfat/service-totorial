package com.mglj.totorial.framework.tool.mq;

/**
 * 消息的处理
 *
 * Created by zsp on 2018/9/1.
 */
public interface MessageListener<T> {

    /**
     * 处理消息
     *
     * @param message
     */
    void handle(T message);

}
