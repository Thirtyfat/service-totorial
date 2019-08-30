package com.mglj.totorial.framework.common.lang;

import java.io.Serializable;

/**
 *
 * @param <TKey>
 * @param <TValue>
 */
public class KeyValue<TKey, TValue> implements Serializable {

	private static final long serialVersionUID = -7570482398422165475L;

	/**
	 *
	 */
	protected TKey key;
	/**
	 *
	 */
	protected TValue value;
	
	public KeyValue() {
		
	}
	
	/**
	 *
	 */
	public KeyValue(TKey key, TValue value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString() {
		return "KeyValue{" +
				"key=" + key +
				", value=" + value +
				'}';
	}

	public TKey getKey() {
		return key;
	}
	public void setKey(TKey key) {
		this.key = key;
	}

	public TValue getValue() {
		return value;
	}
	public void setValue(TValue value) {
		this.value = value;
	}
	
}
