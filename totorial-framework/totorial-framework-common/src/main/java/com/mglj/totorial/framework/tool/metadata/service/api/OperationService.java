package com.mglj.totorial.framework.tool.metadata.service.api;


import com.mglj.totorial.framework.tool.metadata.domain.Operation;

/**
 * Created by zsp on 2018/8/30.
 */
public interface OperationService {

    /**
     * 保存功能描述
     *
     * @param operation
     */
    void saveOperation(Operation operation);

}
