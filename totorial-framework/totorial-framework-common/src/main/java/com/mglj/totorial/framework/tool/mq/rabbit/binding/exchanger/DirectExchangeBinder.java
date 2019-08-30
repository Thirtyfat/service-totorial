package com.mglj.totorial.framework.tool.mq.rabbit.binding.exchanger;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

/**
 * Created by zsp on 2018/9/21.
 */
public class DirectExchangeBinder extends AbstractExchangeBinder {

    public DirectExchangeBinder(RabbitAdmin rabbitAdmin) {
        super(rabbitAdmin);
    }

    @Override
    public Exchange declareExchange(String exchangeName) {
        DirectExchange exchange = new DirectExchange(exchangeName,
                true, false, null);
        rabbitAdmin.declareExchange(exchange);
        return exchange;
    }

    @Override
    public void bind(String routeKey, Exchange exchange, Queue queue) {
        Binding binding = BindingBuilder
                .bind(queue)
                .to((DirectExchange)exchange)
                .with(routeKey);
        rabbitAdmin.declareBinding(binding);
    }
}
