package com.mglj.totorial.framework.tool.mq.rabbit.binding;


import com.mglj.totorial.framework.tool.mq.rabbit.RabbitEvent;

/**
 * Created by zsp on 2018/8/31.
 */
public interface RabbitBinder {

    void bind(RabbitEvent rabbitEvent);

}
