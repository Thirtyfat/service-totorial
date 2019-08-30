package com.mglj.totorial.framework.tool.metadata.service.impl;

import com.mglj.totorial.framework.tool.metadata.domain.Operation;
import com.mglj.totorial.framework.tool.metadata.manager.api.OperationManager;
import com.mglj.totorial.framework.tool.metadata.service.api.OperationService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zsp on 2018/9/3.
 */
public class OperationServiceImpl implements OperationService {

    @Autowired
    private OperationManager operationManager;

    @Override
    public void saveOperation(Operation operation) {
        operationManager.saveOperation(operation);
    }

}
