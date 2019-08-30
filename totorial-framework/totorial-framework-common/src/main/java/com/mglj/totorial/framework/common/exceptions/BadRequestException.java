package com.mglj.totorial.framework.common.exceptions;

/**
 * Created by zsp on 2019/2/18.
 */
public class BadRequestException extends BaseSysException {

    public BadRequestException() {
        super();
    }

    public BadRequestException(String msg) {
        super(msg);
    }

    public BadRequestException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }

}
