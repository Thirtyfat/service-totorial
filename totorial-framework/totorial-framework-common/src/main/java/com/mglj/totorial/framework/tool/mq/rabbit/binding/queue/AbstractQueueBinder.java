package com.mglj.totorial.framework.tool.mq.rabbit.binding.queue;

import org.springframework.amqp.rabbit.core.RabbitAdmin;

/**
 * Created by zsp on 2018/9/25.
 */
public abstract class AbstractQueueBinder implements QueueBinder {

    protected final RabbitAdmin rabbitAdmin;

    protected AbstractQueueBinder(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

}
