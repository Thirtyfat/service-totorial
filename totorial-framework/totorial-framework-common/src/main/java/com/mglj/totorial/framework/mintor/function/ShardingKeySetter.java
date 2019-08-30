package com.mglj.totorial.framework.mintor.function;

/**
 * 分片键的设置逻辑
 *
 * Created by zsp on 2019/3/28.
 */
@FunctionalInterface
public interface ShardingKeySetter {

    /**
     * 根据方法的入参来设置分片键
     *
     * @param arguments             方法入参
     * @param parameterNames        方法入参参数名
     */
    void set(Object arguments, String[] parameterNames);

}
