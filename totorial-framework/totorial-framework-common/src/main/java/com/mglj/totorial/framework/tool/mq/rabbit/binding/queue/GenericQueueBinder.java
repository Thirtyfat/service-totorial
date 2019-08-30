package com.mglj.totorial.framework.tool.mq.rabbit.binding.queue;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * Created by zsp on 2018/9/25.
 */
public class GenericQueueBinder extends AbstractQueueBinder {

    public GenericQueueBinder(RabbitAdmin rabbitAdmin) {
        super(rabbitAdmin);
    }

    @Override
    public Queue declareQueue(String queueName) {
        return declareQueue(queueName, null);
    }

    @Override
    public Queue declareQueue(String queueName, Map<String, Object> arguments) {
        Queue queue;
        if(CollectionUtils.isEmpty(arguments)) {
            queue = new Queue(queueName,
                    true, false, false);
        } else {
            queue = new Queue(queueName,
                    true, false, false, arguments);
        }
        rabbitAdmin.declareQueue(queue);
        return queue;
    }
}
