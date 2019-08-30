package com.mglj.totorial.framework.tool.data.redis.operation;


import com.mglj.totorial.framework.tool.data.redis.RedisOperationEnum;

import java.util.Collection;
import java.util.concurrent.Callable;

/**
 * Created by zsp on 2018/8/23.
 */
public abstract class AbstractOperations implements Operations {

    protected final Operations operations;

    protected AbstractOperations(Operations operations) {
        this.operations = operations;
    }

    public abstract void execute(RedisOperationEnum operation,
                                 String keyPrefix,
                                 Collection<String> keys,
                                 Runnable command,
                                 Object...values);

    public abstract <T> T execute(RedisOperationEnum operation,
                                  String keyPrefix,
                                  Collection<String> keys,
                                  Callable<T> command,
                                  Object...values);

}
