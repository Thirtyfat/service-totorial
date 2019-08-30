package com.mglj.totorial.framework.tool.mq.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mglj.totorial.framework.tool.json.JsonUtils;
import com.mglj.totorial.framework.tool.logging.LogUtils;
import com.mglj.totorial.framework.tool.mq.AbstractConsumer;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * Created by zsp on 2018/9/1.
 */
public class RabbitConsumer<T> extends AbstractConsumer<T> {

    private static final Logger logger = LoggerFactory.getLogger(RabbitConsumer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RabbitEvent rabbitEvent;

    private SimpleMessageListenerContainer msgListenerContainer;
    private MessageConverter messageConverter;
    private Class<T> messageType;

    public RabbitConsumer(RabbitConsumerFactory rabbitConsumerFactory,
                            RabbitEvent rabbitEvent) {
        super(rabbitConsumerFactory, rabbitEvent);
        this.rabbitEvent = rabbitEvent;
    }

    public RabbitConsumer(RabbitConsumerFactory rabbitConsumerFactory,
                          RabbitEvent rabbitEvent,
                          MessageConverter messageConverter) {
        super(rabbitConsumerFactory, rabbitEvent);
        this.rabbitEvent = rabbitEvent;
        this.messageConverter = messageConverter;
    }

    public RabbitConsumer(RabbitConsumerFactory rabbitConsumerFactory,
                          RabbitEvent rabbitEvent,
                          Class<T> messageType) {
        super(rabbitConsumerFactory, rabbitEvent);
        this.rabbitEvent = rabbitEvent;
        this.messageType = messageType;
    }

    @Override
    protected void doInitialize() {
        this.msgListenerContainer = new SimpleMessageListenerContainer();
        this.msgListenerContainer.setConnectionFactory(((RabbitConsumerFactory)consumerFactory).getConnectionFactory());
        Object listener = new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel)
                    throws Exception {
                try {
                    T obj;
                    if(messageConverter != null) {
                        obj = (T) messageConverter.fromMessage(message);
                    } else {
                        byte[] body = message.getBody();
                        if(messageType != null) {
                            obj = JsonUtils.parse(objectMapper, body, messageType);
                        } else {
                            obj = (T) body;
                        }
                    }
                    handleMessage(obj);
                    if(rabbitEvent.isManualAck()) {
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    }
                } catch (Exception e){
                    if (message.getMessageProperties().getRedelivered()) {
                        //消息已重复处理失败,拒绝再次接收
                        LogUtils.info(logger, "消息已重复处理失败,拒绝再次接收；异常为：" + e.getMessage());
                        channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                    } else {
                        //消息再次返回队列处理
                        LogUtils.info(logger, "消息再次返回队列处理；异常为：" + e.getMessage());
                        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                    }
                }
            }
        };
        if(rabbitEvent.isManualAck()) {
            this.msgListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        } else {
            this.msgListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        }
        this.msgListenerContainer.setMessageListener(listener);
        this.msgListenerContainer.setPrefetchCount(rabbitEvent.getPrefetchCount());
        this.msgListenerContainer.setConcurrentConsumers(rabbitEvent.getConcurrentConsumers());
        this.msgListenerContainer.setTxSize(rabbitEvent.getTxSize());
        this.msgListenerContainer.setQueueNames(rabbitEvent.getQueueName());
        this.msgListenerContainer.start();
    }

    @Override
    protected void doDestroy() {
        if(this.msgListenerContainer != null) {
            this.msgListenerContainer.destroy();
        }
    }

}
