package com.mglj.totorial.framework.tool.dao.api;


import com.mglj.totorial.framework.tool.metadata.domain.Operation;

import java.util.Collection;
import java.util.List;

/**
 * Created by zsp on 2018/8/30.
 */
public interface OperationDao {

    int saveOperation(Operation operation);

    List<Operation> listOperationByHashCode(int hashCode);

    List<Operation> getOperationListByHashCodeAndOffset(Collection<Operation> collection);

}
