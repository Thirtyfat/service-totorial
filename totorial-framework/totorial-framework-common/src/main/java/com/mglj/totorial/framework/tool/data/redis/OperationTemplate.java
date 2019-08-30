package com.mglj.totorial.framework.tool.data.redis;

/**
 * 访问redis的模板工具
 *
 * Created by zsp on 2018/8/23.
 */
public interface OperationTemplate {

    /**
     * 获取应用空间
     * @return
     */
    String getNamespace();

    /**
     * 设置应用空间
     *
     * @param value
     */
    void setNamespace(String value);

    /**
     * 获取业务领域名称
     * @return
     */
    String getDomain();

    /**
     * 设置业务领域名称
     * @param value
     */
    void setDomain(String value);

    /**
     * 获取批量操作的记录数
     *
     * @return
     */
    int getMultiOperationBatchSize();

    /**
     * 设置批量操作的记录数
     *
     * @param value
     */
    void setMultiOperationBatchSize(int value);

    /**
     * 是否开启键过期时间的上下浮动特性
     *
     * @return
     */
    boolean isEnableKeyExpiredVolatility();

    /**
     * 设置开启键过期时间的上下浮动特性
     *
     * @return
     */
    void setEnableKeyExpiredVolatility(boolean value);

}
