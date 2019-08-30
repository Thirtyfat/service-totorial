package com.mglj.totorial.framework.common.exceptions;

/**
 * Created by zsp on 2018/8/24.
 */
public class UnsupportedDataException extends BaseSysException {

    private static final long serialVersionUID = 8534719281970952336L;

    public UnsupportedDataException() {
        super();
    }

    public UnsupportedDataException(String msg) {
        super(msg);
    }

    public UnsupportedDataException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UnsupportedDataException(Throwable cause) {
        super(cause);
    }

}
