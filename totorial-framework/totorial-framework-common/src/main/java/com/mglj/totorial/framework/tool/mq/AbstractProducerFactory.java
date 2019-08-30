package com.mglj.totorial.framework.tool.mq;

import com.mglj.totorial.framework.tool.concurrent.threading.ExecutorServiceFactory;
import com.mglj.totorial.framework.tool.concurrent.threading.FixedExecutorServiceFactory;
import com.mglj.totorial.framework.tool.sequence.SequenceGenerator;
import org.springframework.beans.factory.DisposableBean;

import java.util.concurrent.ExecutorService;

/**
 * Created by zsp on 2018/9/4.
 */
public abstract class AbstractProducerFactory implements ProducerFactory, DisposableBean {

    protected final MessageManager messageManager;
    protected final SequenceGenerator sequenceGenerator;
    private final ExecutorServiceFactory executorServiceFactory;

    public AbstractProducerFactory(MessageManager messageManager,
                                   SequenceGenerator sequenceGenerator) {
        this.messageManager = messageManager;
        this.sequenceGenerator = sequenceGenerator;
        this.executorServiceFactory = new FixedExecutorServiceFactory("mq-send");
    }

    @Override
    public ExecutorService getSendThreadPool() {
        return executorServiceFactory.get();
    }

    @Override
    public MessageManager getMessageManager() {
        return messageManager;
    }

    @Override
    public SequenceGenerator getSequenceGenerator() {
        return sequenceGenerator;
    }

    @Override
    public void destroy() throws Exception {
        executorServiceFactory.destroy();
    }

}
