package com.mglj.totorial.framework.tools.exceptions;

/**
 * Created by yj on 2018/9/14.
 */
public class ObjectNotFoundException extends BaseSysException {

    private static final long serialVersionUID = -8179598019161269064L;

    public ObjectNotFoundException(){
        super();
    }

    public ObjectNotFoundException(String msg) {
        super(msg);
    }

    public ObjectNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ObjectNotFoundException(Throwable cause) {
        super(cause);
    }

}
