package com.mglj.totorial.framework.tools.exceptions;

/**
 * 基础系统异常
 * 
 * @author yj
 */
public class BaseSysException extends RuntimeException {
	
	private static final long serialVersionUID = -7678875967251181234L;

	public BaseSysException() {
		super();
	}

	public BaseSysException(String msg) {
		super(msg);
	}
	
	public BaseSysException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public BaseSysException(Throwable cause) {
		super(cause);
	}
	
}
