package com.mglj.totorial.framework.tools.model;

import java.io.Serializable;

/**
 * 键值对
 * 
 * @author yj
 * @date 2016-7-13
 */
public class StringKeyValue implements Serializable {

	private static final long serialVersionUID = 7158739060930863332L;
	
	/**
	 * 键
	 */
	private String key;
	/**
	 * 值
	 */
	private String value;
	
	public StringKeyValue() {
		
	}
	
	/**
	 * 构建一个键值对
	 */
	public StringKeyValue(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return ((key != null) ? key.toString(): key) + ": " + ((value != null) ? value.toString(): value);
	}
	
}
