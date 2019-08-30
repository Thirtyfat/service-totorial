package com.mglj.totorial.framework.tool.mq;


import com.mglj.totorial.framework.tool.sequence.SequenceGenerator;

/**
 * Created by zsp on 2018/9/4.
 */
public abstract class AbstractConsumerFactory implements ConsumerFactory {

    protected final MessageManager messageManager;
    protected final SequenceGenerator sequenceGenerator;

    public AbstractConsumerFactory(MessageManager messageManager,
                                   SequenceGenerator sequenceGenerator) {
        this.messageManager = messageManager;
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public MessageManager getMessageManager() {
        return messageManager;
    }

    @Override
    public SequenceGenerator getSequenceGenerator() {
        return sequenceGenerator;
    }

}
