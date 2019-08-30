package com.mglj.totorial.framework.tool.mq.rabbit.binding.queue;

import org.springframework.amqp.core.Queue;

import java.util.Map;

/**
 * Created by zsp on 2018/9/25.
 */
public interface QueueBinder {

    Queue declareQueue(String queueName);

    Queue declareQueue(String queueName, Map<String, Object> arguments);

}
