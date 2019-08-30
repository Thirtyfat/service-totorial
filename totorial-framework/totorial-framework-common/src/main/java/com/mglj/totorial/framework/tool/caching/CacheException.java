package com.mglj.totorial.framework.tool.caching;


import com.mglj.totorial.framework.common.exceptions.BaseSysException;

/**
 * Created by zsp on 2018/11/19.
 */
public class CacheException extends BaseSysException {

    public CacheException() {
        super();
    }

    public CacheException(String msg) {
        super(msg);
    }

    public CacheException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }

}
