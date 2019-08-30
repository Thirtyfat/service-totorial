package com.mglj.totorial.framework.tool.mq.rabbit;

import com.mglj.totorial.framework.common.exceptions.UnsupportedDataTypeException;
import com.mglj.totorial.framework.tool.mq.AbstractConsumerFactory;
import com.mglj.totorial.framework.tool.mq.Consumer;
import com.mglj.totorial.framework.tool.mq.MqEvent;
import com.mglj.totorial.framework.tool.sequence.SequenceGenerator;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.util.StringUtils;

/**
 * Created by zsp on 2018/9/1.
 */
public class RabbitConsumerFactory extends AbstractConsumerFactory {

    private final CachingConnectionFactory connectionFactory;

    public RabbitConsumerFactory(CachingConnectionFactory connectionFactory,
                                 RabbitMessageManager rabbitMessageManager,
                                 SequenceGenerator sequenceGenerator) {
        super(rabbitMessageManager, sequenceGenerator);
        this.connectionFactory = connectionFactory;
    }

    @Override
    public <T> Consumer<T> getConsumer(MqEvent event) {
        RabbitEvent rabbitEvent = (RabbitEvent)event;
        validAndPrepare(rabbitEvent);
        return new RabbitConsumer<>(this, rabbitEvent);
    }

    @Override
    public <T> Consumer<T> getConsumer(MqEvent event, MessageConverter messageConverter) {
        RabbitEvent rabbitEvent = (RabbitEvent)event;
        validAndPrepare(rabbitEvent);
        return new RabbitConsumer<>(this, rabbitEvent, messageConverter);
    }

    @Override
    public <T> Consumer<T> getConsumer(MqEvent event, Class<T> messageType) {
        RabbitEvent rabbitEvent = (RabbitEvent)event;
        validAndPrepare(rabbitEvent);
        return new RabbitConsumer<>(this, rabbitEvent, messageType);
    }

    public CachingConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    private void validAndPrepare(RabbitEvent rabbitEvent) {
        if(rabbitEvent == null) {
            throw new UnsupportedDataTypeException("The event should be instance of RabbitEvent.");
        }
        if(!StringUtils.hasText(rabbitEvent.getExchangeName())) {
            throw new IllegalArgumentException("The exchangeName is null or empty.");
        }
        if(!StringUtils.hasText(rabbitEvent.getQueueName())) {
            throw new IllegalArgumentException("The queueName is null or empty.");
        }
        if(!messageManager.isRegistrated(rabbitEvent)) {
            messageManager.registrate(rabbitEvent);
        }
    }

}
