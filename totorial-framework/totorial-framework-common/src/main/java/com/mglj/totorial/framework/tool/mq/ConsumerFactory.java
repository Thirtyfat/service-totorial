package com.mglj.totorial.framework.tool.mq;

import com.mglj.totorial.framework.tool.sequence.SequenceGenerator;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * Created by zsp on 2018/8/31.
 */
public interface ConsumerFactory {

    /**
     *
     * @param event
     * @param <T>
     * @return
     */
    <T> Consumer<T> getConsumer(MqEvent event);

    /**
     *
     * @param event
     * @param messageConverter
     * @param <T>
     * @return
     */
    <T> Consumer<T> getConsumer(MqEvent event, MessageConverter messageConverter);

    /**
     *
     * @param event
     * @param messageType
     * @param <T>
     * @return
     */
    <T> Consumer<T> getConsumer(MqEvent event, Class<T> messageType);

    /**
     *
     * @return
     */
    MessageManager getMessageManager();

    /**
     *
     * @return
     */
    SequenceGenerator getSequenceGenerator();

}
