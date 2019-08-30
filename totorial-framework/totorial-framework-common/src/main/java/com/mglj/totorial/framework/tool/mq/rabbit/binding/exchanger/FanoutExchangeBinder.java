package com.mglj.totorial.framework.tool.mq.rabbit.binding.exchanger;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

/**
 * Created by zsp on 2018/9/21.
 */
public class FanoutExchangeBinder extends AbstractExchangeBinder {

    public FanoutExchangeBinder(RabbitAdmin rabbitAdmin) {
        super(rabbitAdmin);
    }

    @Override
    public Exchange declareExchange(String exchangeName) {
        FanoutExchange exchange = new FanoutExchange(exchangeName,
                true, false, null);
        rabbitAdmin.declareExchange(exchange);
        return exchange;
    }

    @Override
    public void bind(String routeKey, Exchange exchange, Queue queue) {
        Binding binding = BindingBuilder
                .bind(queue)
                .to((FanoutExchange)exchange);
        rabbitAdmin.declareBinding(binding);
    }
}
