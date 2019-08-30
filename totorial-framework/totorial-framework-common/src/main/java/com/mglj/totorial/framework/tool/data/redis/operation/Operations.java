package com.mglj.totorial.framework.tool.data.redis.operation;


import com.mglj.totorial.framework.tool.data.redis.RedisOperationEnum;

import java.util.Collection;
import java.util.concurrent.Callable;

/**
 * Created by zsp on 2018/8/9.
 */
public interface Operations {

    /**
     *
     * @param operation
     * @param keyPrefix
     * @param keys
     * @param command
     * @param values
     */
    void execute(RedisOperationEnum operation,
                 String keyPrefix,
                 Collection<String> keys,
                 Runnable command,
                 Object... values);

    /**
     *
     * @param operation
     * @param keyPrefix
     * @param keys
     * @param command
     * @param values
     * @param <T>
     * @return
     */
    <T> T execute(RedisOperationEnum operation,
                  String keyPrefix,
                  Collection<String> keys,
                  Callable<T> command,
                  Object... values);

}
