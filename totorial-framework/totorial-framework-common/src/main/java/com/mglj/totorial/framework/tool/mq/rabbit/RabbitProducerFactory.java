package com.mglj.totorial.framework.tool.mq.rabbit;

import com.mglj.totorial.framework.common.exceptions.UnsupportedDataTypeException;
import com.mglj.totorial.framework.tool.mq.AbstractProducerFactory;
import com.mglj.totorial.framework.tool.mq.MqEvent;
import com.mglj.totorial.framework.tool.mq.Producer;
import com.mglj.totorial.framework.tool.sequence.SequenceGenerator;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.util.StringUtils;

/**
 * Created by zsp on 2018/8/31.
 */
public class RabbitProducerFactory extends AbstractProducerFactory {

    private final CachingConnectionFactory connectionFactory;

    public RabbitProducerFactory(CachingConnectionFactory connectionFactory,
                                 RabbitMessageManager rabbitMessageManager,
                                 SequenceGenerator sequenceGenerator) {
        super(rabbitMessageManager, sequenceGenerator);
        this.connectionFactory = connectionFactory;
    }

    @Override
    public <T> Producer<T> getProducer(MqEvent event) {
       return getProducer(event, null);
    }

    @Override
    public <T> Producer<T> getProducer(MqEvent event, MessageConverter messageConverter) {
        RabbitEvent rabbitEvent = (RabbitEvent)event;
        if(rabbitEvent == null) {
            throw new UnsupportedDataTypeException("The event should be instance of RabbitEvent.");
        }
        if(!StringUtils.hasText(rabbitEvent.getExchangeName())) {
            throw new IllegalArgumentException("The exchangeName is null or empty.");
        }
        if(!StringUtils.hasText(rabbitEvent.getQueueName())) {
            throw new IllegalArgumentException("The queueName is null or empty.");
        }
        ExchangeTypeEnum exchangeType = rabbitEvent.getExchangeType();
        if (exchangeType == null) {
            rabbitEvent.setExchangeType(ExchangeTypeEnum.DIRECT);
        }
        if(!messageManager.isRegistrated(rabbitEvent)) {
            messageManager.registrate(rabbitEvent);
        }
        return new RabbitProducer<>(this, (RabbitEvent)event, messageConverter);
    }

    public CachingConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

}
