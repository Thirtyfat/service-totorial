package com.mglj.totorial.framework.tool.metadata.service.impl;

import com.mglj.totorial.framework.common.exceptions.IncorrectAffectedRowsException;
import com.mglj.totorial.framework.tool.metadata.domain.Operation;
import com.mglj.totorial.framework.tool.metadata.manager.api.OperationManager;
import com.mglj.totorial.framework.tool.metadata.manager.api.OperationSyncManager;
import com.mglj.totorial.framework.tool.metadata.service.api.OperationSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by zsp on 2018/9/4.
 */
public class OperationSyncServiceImpl implements OperationSyncService {

    @Autowired
    private OperationManager operationManager;
    @Autowired
    private OperationSyncManager operationSyncManager;

    @Override
    public Operation getOrSaveOperation(String className, String methodName,
                                        String title, String group) {
        int hashCode = Operation.getHashCode(className, methodName);
        for (int loop = 0; ; loop++) {
            List<Operation> operationList = operationManager.listOperationByHashCode(hashCode);
            if (!CollectionUtils.isEmpty(operationList)) {
                for (Operation operation : operationList) {
                    if (className.equals(operation.getClassName())
                            && methodName.equals(operation.getMethodName())) {
                        return operation;
                    }
                }
            }
            Operation operation = new Operation();
            operation.setClassName(className);
            operation.setMethodName(methodName);
            operation.setHashCode(hashCode);
            operation.setOffset(CollectionUtils.isEmpty(operationList) ? 0 : operationList.size());
            operation.setTitle(title);
            operation.setGroup(group);
            int affectedRows = operationManager.saveOperation(operation);
            if (affectedRows == 0) {
                if(loop > 5) {
                    throw new IncorrectAffectedRowsException("尝试增加系统功能操作的次数已达到最大数，请重新尝试");
                }
                continue;
            }
            operationSyncManager.syncOperation(operation);
            return operation;
        }
    }

}
