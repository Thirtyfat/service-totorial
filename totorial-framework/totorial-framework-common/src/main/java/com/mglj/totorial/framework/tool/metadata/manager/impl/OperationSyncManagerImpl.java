package com.mglj.totorial.framework.tool.metadata.manager.impl;


import com.mglj.totorial.framework.tool.metadata.domain.Operation;
import com.mglj.totorial.framework.tool.metadata.manager.api.OperationSyncManager;
import com.mglj.totorial.framework.tool.mq.Producer;

/**
 * Created by zsp on 2018/9/4.
 */
public class OperationSyncManagerImpl implements OperationSyncManager {

    private Producer<Operation> operationProducer;
    public void setOperationProducer(Producer<Operation> operationProducer) {
        this.operationProducer = operationProducer;
    }

    @Override
    public void syncOperation(Operation operation) {
        operationProducer.asyncSend(operation);
    }
}
