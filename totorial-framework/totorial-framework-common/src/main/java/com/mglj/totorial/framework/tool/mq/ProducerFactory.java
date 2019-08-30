package com.mglj.totorial.framework.tool.mq;

import com.mglj.totorial.framework.tool.sequence.SequenceGenerator;
import org.springframework.amqp.support.converter.MessageConverter;

import java.util.concurrent.ExecutorService;

/**
 * 消息生产方工厂
 *
 * Created by zsp on 2018/8/31.
 */
public interface ProducerFactory {

    /**
     *
     * @param event
     * @param <T>
     * @return
     */
    <T> Producer<T> getProducer(MqEvent event);

    /**
     *
     * @param event
     * @param messageConverter
     * @param <T>
     * @return
     */
    <T> Producer<T> getProducer(MqEvent event, MessageConverter messageConverter);

    /**
     *
     * @return
     */
    ExecutorService getSendThreadPool();

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
