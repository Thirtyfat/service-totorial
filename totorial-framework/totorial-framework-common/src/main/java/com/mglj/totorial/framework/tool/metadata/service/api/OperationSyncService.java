package com.mglj.totorial.framework.tool.metadata.service.api;


import com.mglj.totorial.framework.tool.metadata.domain.Operation;

/**
 * Created by zsp on 2018/9/4.
 */
public interface OperationSyncService {

    /**
     * 获取或保存功能描述
     *
     * @param className     类名
     * @param methodName    方法名
     * @param title         功能名称
     * @param group         功能分组
     * @return
     */
    Operation getOrSaveOperation(String className, String methodName,
                                 String title, String group);

}
