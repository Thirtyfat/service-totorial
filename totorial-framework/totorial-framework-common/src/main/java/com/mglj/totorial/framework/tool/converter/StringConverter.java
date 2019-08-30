package com.mglj.totorial.framework.tool.converter;

/**
 * 字符串转换工具
 *
 * @param <T>
 */
public interface StringConverter<T> {

	/**
	 * 序列化为字符串
	 *
	 * @param value
	 * @return
	 */
	String serialize(T value);

	/**
	 * 反序列化为对象
	 *
	 * @param value
	 * @param clazz
	 * @return
	 */
	T deserialize(String value, Class<T> clazz);
	
}
