package com.mglj.totorial.framework.common.lang;

import java.util.Objects;

/**
 * 缓存对象，封装实际的缓存数据。
 * 
 * @author zsp
 *
 * @param <K>
 * @param <V>
 */
public final class CacheObject<K, V> {

	/**
	 * 防止缓存击穿的空值；用于设置缓存和背景数据源（例如数据库）都不存在键值，以防止缓存击穿
	 */
	public final static String EMPTY_VALUE_FOR_BREAKDOWN_PREVENT = ".";
	
	private final K key;
	private V value;
	private boolean empty;
	
	/**
	 * 
	 * @param key
	 */
	public CacheObject(K key) {
		Objects.requireNonNull(key);
		this.key = key;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public CacheObject(K key, V value) {
		this(key);
		this.value = value;
	}
	
	/**
	 * 缓存键
	 * 
	 * @return
	 */
	public K getKey() {
		return key;
	}

	/**
	 * 获取实际的缓存对象
	 *
	 * @return
	 */
	public V getValue() {
		return value;
	}

	/**
	 * 作为空值缓存，用于防止缓存击穿
	 * 
	 * @return
	 */
	public CacheObject<K, V> asEmpty() {
		this.empty = true;
		return this;
	}

	/**
	 * 判断是否是空值缓存
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return empty;
	}
	
	/**
	 * 判断实际缓存的对象是否为null
	 * @return
	 */
	public boolean isNull() {
		return !empty && value == null;
	}

	/**
     * 判断数据是否是为防缓存击穿的空值
	 *
	 * @param value
     * @return
     */
	public static boolean isEmptyValueForBreakdownPrevent(Object value) {
		if(value == null) {
			return false;
		}
		return Objects.equals(EMPTY_VALUE_FOR_BREAKDOWN_PREVENT, value);
	}

}
