package com.mglj.totorial.framework.tool.metadata.manager.api;


import com.mglj.totorial.framework.tool.metadata.domain.Operation;

import java.util.List;

/**
 * Created by zsp on 2018/9/3.
 */
public interface OperationManager {

    int saveOperation(Operation operation);

    List<Operation> listOperationByHashCode(int hashCode);

}
