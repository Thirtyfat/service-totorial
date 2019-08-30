package com.mglj.totorial.framework.common.exceptions;

/**
 * Created by zsp on 2018/8/7.
 */
public class ValidationException extends BaseBizException {

    private static final long serialVersionUID = -8406407205363762293L;

    private String errorCode;

    public ValidationException(String msg) {
        super(msg);
    }

    public ValidationException(String errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public ValidationException(String msg, Throwable cause) {
        super(cause);
    }

    public ValidationException(String errorCode, String msg, Throwable cause) {
        super(msg, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
