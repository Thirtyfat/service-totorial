package com.mglj.totorial.framework.common.exceptions;

/**
 * Created by zsp on 2018/8/24.
 */
public class UnsupportedDataTypeException extends BaseSysException {

    private static final long serialVersionUID = 3009200574107253532L;

    public UnsupportedDataTypeException() {
        super();
    }

    public UnsupportedDataTypeException(String msg) {
        super(msg);
    }

    public UnsupportedDataTypeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UnsupportedDataTypeException(Throwable cause) {
        super(cause);
    }

    public UnsupportedDataTypeException(Class<?> requiredType) {
        this("The require type is " + requiredType.getName());
    }

    public UnsupportedDataTypeException(Class<?> requiredType, Class<?> factType) {
        this("The require type is " + requiredType.getName() + ", but fact type is " + factType.getName());
    }

}
