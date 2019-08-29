package com.mglj.totorial.framework.tools.exceptions;

/**
 * 不正确的影响行数异常
 * 
 * @author yj
 */
public class IncorrectAffectedRowsException extends BaseSysException {
	
	private static final long serialVersionUID = 704392971437718929L;
	
	private Integer expect;
	private Integer actual;

	public IncorrectAffectedRowsException() {
		super();
	}

	public IncorrectAffectedRowsException(String msg) {
		super(msg);
	}
	
	public IncorrectAffectedRowsException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public IncorrectAffectedRowsException(Throwable cause) {
		super(cause);
	}
	
	public IncorrectAffectedRowsException(int expect, int actual) {
		this(null, expect, actual);
	}
	
	public IncorrectAffectedRowsException(String msg, int expect, int actual) {
		this(msg);
		
		this.expect = expect;
		this.actual = actual;
	}

	public Integer getExpect() {
		return expect;
	}

	public Integer getActual() {
		return actual;
	}
	
}
