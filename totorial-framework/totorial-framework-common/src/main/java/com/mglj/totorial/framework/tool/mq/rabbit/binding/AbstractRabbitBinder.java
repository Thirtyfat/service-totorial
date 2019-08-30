package com.mglj.totorial.framework.tool.mq.rabbit.binding;

import com.mglj.totorial.framework.tool.mq.rabbit.ExchangeTypeEnum;
import com.mglj.totorial.framework.tool.mq.rabbit.RabbitEvent;
import com.mglj.totorial.framework.tool.mq.rabbit.binding.exchanger.DirectExchangeBinder;
import com.mglj.totorial.framework.tool.mq.rabbit.binding.exchanger.ExchangeBinder;
import com.mglj.totorial.framework.tool.mq.rabbit.binding.exchanger.FanoutExchangeBinder;
import com.mglj.totorial.framework.tool.mq.rabbit.binding.exchanger.TopicExchangeBinder;
import com.mglj.totorial.framework.tool.mq.rabbit.binding.queue.GenericQueueBinder;
import com.mglj.totorial.framework.tool.mq.rabbit.binding.queue.QueueBinder;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zsp on 2018/8/31.
 */
public abstract class AbstractRabbitBinder implements RabbitBinder {

    protected final RabbitAdmin rabbitAdmin;
    private final Map<ExchangeTypeEnum, ExchangeBinder> exchangeBinderMap;
    private final QueueBinder queueBinder;

    protected AbstractRabbitBinder(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
        Map<ExchangeTypeEnum, ExchangeBinder> exchangeBinderMap0 = new HashMap<>();
        exchangeBinderMap0.put(ExchangeTypeEnum.DIRECT, new DirectExchangeBinder(rabbitAdmin));
        exchangeBinderMap0.put(ExchangeTypeEnum.FANOUT, new FanoutExchangeBinder(rabbitAdmin));
        exchangeBinderMap0.put(ExchangeTypeEnum.TOPIC, new TopicExchangeBinder(rabbitAdmin));
        this.exchangeBinderMap = exchangeBinderMap0;
        this.queueBinder = new GenericQueueBinder(rabbitAdmin);
    }

    protected ExchangeBinder getExchangeBinder(ExchangeTypeEnum exchangeType) {
        return exchangeBinderMap.get(exchangeType);
    }

    protected QueueBinder getQueueBinder() {
        return queueBinder;
    }

    @Override
    public abstract void bind(RabbitEvent rabbitEvent);

}
