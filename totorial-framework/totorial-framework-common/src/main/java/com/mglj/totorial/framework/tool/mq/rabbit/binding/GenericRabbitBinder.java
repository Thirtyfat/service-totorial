package com.mglj.totorial.framework.tool.mq.rabbit.binding;

import com.mglj.totorial.framework.tool.mq.rabbit.RabbitEvent;
import com.mglj.totorial.framework.tool.mq.rabbit.binding.exchanger.ExchangeBinder;
import com.mglj.totorial.framework.tool.mq.rabbit.binding.queue.QueueBinder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.mglj.totorial.framework.tool.mq.rabbit.util.NameUtils.KEY_DEAD_LETTER_EXCHANGE;
import static com.mglj.totorial.framework.tool.mq.rabbit.util.NameUtils.KEY_DEAD_LETTER_ROUTING_KEY;
import static com.mglj.totorial.framework.tool.mq.rabbit.util.NameUtils.KEY_MESSAGE_TTL_;


/**
 * Created by zsp on 2018/9/25.
 */
public class GenericRabbitBinder extends AbstractRabbitBinder {

    public GenericRabbitBinder(RabbitAdmin rabbitAdmin) {
        super(rabbitAdmin);
    }

    @Override
    public void bind(RabbitEvent rabbitEvent) {
        ExchangeBinder exchangeBinder = getExchangeBinder(rabbitEvent.getExchangeType());
        QueueBinder queueBinder = getQueueBinder();

        Exchange exchange = exchangeBinder.declareExchange(rabbitEvent.getExchangeName());
        Map<String, Object> arguments = new HashMap<>();
        int survivalMillis = rabbitEvent.getSurvivalMillis();
        if(survivalMillis > 0) {
            arguments.put(KEY_MESSAGE_TTL_, survivalMillis);
        }
        String deadLetterExchangeName = rabbitEvent.getDeadLetterExchangeName();
        String deadLetterRouteKey = rabbitEvent.getDeadLetterRouteKey();
        if(StringUtils.hasText(deadLetterExchangeName)
                && StringUtils.hasText(deadLetterRouteKey)) {
            arguments.put(KEY_DEAD_LETTER_EXCHANGE, deadLetterExchangeName);
            arguments.put(KEY_DEAD_LETTER_ROUTING_KEY, deadLetterRouteKey);
        }
        Queue queue = queueBinder.declareQueue(rabbitEvent.getQueueName(), arguments);
        exchangeBinder.bind(rabbitEvent.getRouteKey(), exchange, queue);
    }
}
