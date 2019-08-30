package com.mglj.totorial.framework.tool.mq;

/**
 * Created by zsp on 2018/8/31.
 */
public interface Consumer<T> {

    void addEventListener(MessageListener listener);

    void removeEventListener(MessageListener listener);

}
