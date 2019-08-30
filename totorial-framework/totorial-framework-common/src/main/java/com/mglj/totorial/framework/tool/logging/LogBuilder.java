package com.mglj.totorial.framework.tool.logging;


import com.mglj.totorial.framework.common.lang.Result;

/**
 * 日志构建工具
 *
 */
public class LogBuilder {

	private final static String RETURN_LABEL = "return : ";
	private final static String ARG_LABEL = "arg";
	private static String NEW_LINE = null;

	static {
		NEW_LINE = System.getProperty("line.separator");
	}

	private final StringBuilder builder = new StringBuilder(256);

	private int argumentCount;

	public final static LogBuilder create() {
		return new LogBuilder();
	}

	@Override
	public final String toString() {
		return builder.toString();
	}

	public final LogBuilder setMessage(String message) {
		if(message != null) {
			builder.append(message);
		}
		return this;
	}

	public final LogBuilder header() {
		builder.append(NEW_LINE);
		builder.append("----------log begin----------")
				.append(NEW_LINE);
		return this;
	}

	public final LogBuilder footer() {
		builder.append("----------log   end----------");
		return this;
	}

	public final LogBuilder addArgument(Object...arguments) {
		if(arguments != null) {
			for(int i = 0, len = arguments.length; i < len; i++, argumentCount++) {
				builder.append(ARG_LABEL)
						.append(String.format("% 4d", argumentCount))
						.append(": ")
						.append(arguments[i])
						.append(NEW_LINE);
			}
		}
		return this;
	}

	public final LogBuilder setReturnObject(Object returnObject) {
		if(returnObject != null) {
			builder.append(RETURN_LABEL)
					.append(returnObject)
					.append(NEW_LINE);
		}
		return this;
	}

	public final <R> String from(Result<R> result) {
		if(result != null) {
			setResultCodeAndMsg(result);
			R object = result.getResult();
			if(object != null) {
				header();
				setReturnObject(object);
				footer();
			}
		}
		return builder.toString();
	}

	public final <R> String from(Result<R> result, Object...arguments) {
		return setResultCodeAndMsg(result)
				.header()
				.addArgument(arguments)
				.setResultReturnObject(result)
				.footer()
				.toString();
	}

	public final String from(String message, Object returnObject, Object...arguments) {
		return setMessage(message)
				.header()
				.addArgument(arguments)
				.setReturnObject(returnObject)
				.footer()
				.toString();
	}

	private <R> LogBuilder setResultCodeAndMsg(Result<R> result) {
		if(result != null) {
			builder.append(result.getMsg())
					.append("[")
					.append(result.getCode())
					.append("]");
		}
		return this;
	}

	private <R> LogBuilder setResultReturnObject(Result<R> result) {
		if(result != null) {
			builder.append(RETURN_LABEL)
					.append(result.getResult())
					.append(NEW_LINE);
		}
		return this;
	}

}
