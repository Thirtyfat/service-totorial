package com.mglj.totorial.framework.mintor;

public class DataSourceException extends RuntimeException {

    private static final long serialVersionUID = -7678875967251181228L;

    public DataSourceException() {
        super();
    }

    public DataSourceException(String msg) {
        super(msg);
    }

    public DataSourceException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DataSourceException(Throwable cause) {
        super(cause);
    }

}
