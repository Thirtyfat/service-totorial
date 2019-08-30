package com.mglj.totorial.framework.tool.data.redis;


import com.mglj.totorial.framework.common.exceptions.BaseSysException;

/**
 * Created by zsp on 2018/8/23.
 */
public class RedisOperationException extends BaseSysException {

    private static final long serialVersionUID = 1213230022160407259L;

    public RedisOperationException() {
        super();
    }

    public RedisOperationException(String msg) {
        super(msg);
    }

    public RedisOperationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RedisOperationException(Throwable cause) {
        super(cause);
    }

}
