package com.mglj.totorial.framework.tool.mq.rabbit.binding.exchanger;

import org.springframework.amqp.rabbit.core.RabbitAdmin;

/**
 * Created by zsp on 2018/9/21.
 */
public abstract class AbstractExchangeBinder implements ExchangeBinder {

    protected final RabbitAdmin rabbitAdmin;

    protected AbstractExchangeBinder(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

}
