package com.mglj.totorial.framework.tool.mq.rabbit;

import com.mglj.totorial.framework.tool.mq.AbstractProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * Created by zsp on 2018/8/31.
 */
public class RabbitProducer<T> extends AbstractProducer<T> {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitEvent rabbitEvent;

    public RabbitProducer(RabbitProducerFactory rabbitProducerFactory,
                          RabbitEvent rabbitEvent) {
        this(rabbitProducerFactory, rabbitEvent, null);
    }

    public RabbitProducer(RabbitProducerFactory rabbitProducerFactory,
                          RabbitEvent rabbitEvent,
                          MessageConverter messageConverter) {
        super(rabbitProducerFactory, rabbitEvent);
        this.rabbitTemplate = new RabbitTemplate(rabbitProducerFactory.getConnectionFactory());
        this.rabbitEvent = rabbitEvent;
        if (messageConverter != null) {
            this.rabbitTemplate.setMessageConverter(messageConverter);
        }
    }

    @Override
    public void sendMessage(T message) {
        rabbitTemplate.convertAndSend(rabbitEvent.getExchangeName(),
                rabbitEvent.getRouteKey(), message);
    }

}
