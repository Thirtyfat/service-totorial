package com.mglj.totorial.framework.tool.op;

/**
 * 防止频繁操作
 *
 * Created by zsp on 2018/11/8.
 */
public interface FrequentlyOperationPreventHandler {

    public final static String DOMAIN_FREQUENTLY_OPERATION_PREVENT = "frequently-op-prevent";

    /**
     *
     * @param requestId
     * @param seconds
     * @return
     */
    boolean validate(String requestId, long seconds);

}
