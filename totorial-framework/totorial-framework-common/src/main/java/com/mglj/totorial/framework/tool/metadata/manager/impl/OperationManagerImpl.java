package com.mglj.totorial.framework.tool.metadata.manager.impl;

import com.mglj.totorial.framework.tool.dao.api.OperationDao;
import com.mglj.totorial.framework.tool.metadata.domain.Operation;
import com.mglj.totorial.framework.tool.metadata.manager.api.OperationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsp on 2018/9/3.
 */
public class OperationManagerImpl implements OperationManager {

    @Autowired
    private OperationDao operationDao;

    @Override
    public int saveOperation(Operation operation) {
        return operationDao.saveOperation(operation);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public List<Operation> listOperationByHashCode(int hashCode) {
        return operationDao.listOperationByHashCode(hashCode);
    }
}
