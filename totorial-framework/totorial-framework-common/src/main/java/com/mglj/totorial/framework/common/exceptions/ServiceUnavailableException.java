package com.mglj.totorial.framework.common.exceptions;

/**
 * 服务不可用引发的异常
 * 
 * @date 2016-03-15
 * @author zsp
 */
public class ServiceUnavailableException extends BaseSysException {

	private static final long serialVersionUID = -4463926882168556613L;
	
	public ServiceUnavailableException(){
		super();
	}

	public ServiceUnavailableException(String msg) {
		super(msg);
	}
	
	public ServiceUnavailableException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public ServiceUnavailableException(Throwable cause) {
		super(cause);
	}

}
