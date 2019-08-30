package com.mglj.totorial.framework.tool.mq.rabbit.binding.exchanger;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;

/**
 * Created by zsp on 2018/9/21.
 */
public interface ExchangeBinder {

    Exchange declareExchange(String exchangeName);

    void bind(String routeKey, Exchange exchange, Queue queue);

}
