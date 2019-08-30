package com.mglj.totorial.framework.tool.caching;


import com.mglj.totorial.framework.tool.caching.provider.CacheProvider;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 缓存
 *
 * @param <K>
 * @param <V>
 */
public interface Cache<K, V> {
	
	/**
	 * 获取缓存名
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 设置缓存名
	 *
	 * @param value
	 */
	void setName(String value);

	/**
	 * 获取缓存描述
	 *
	 * @return
	 */
	String getDescription();

	/**
	 * 设置缓存描述
	 *
	 * @param value
	 */
	void setDescription(String value);

	/**
	 * 获取缓存失效时间（秒），0表示缓存不失效。
	 *
	 * @return
	 */
	int getExpiredSeconds();
	
	/**
	 * 设置缓存失效时间（秒）
	 * 
	 * @param value
	 */
	void setExpiredSeconds(int value);

	/**
	 * 获取是否开启防止缓存击穿
	 * @return
	 */
	boolean isEnableBreakdownPrevent();

	/**
	 * 设置是否开启防止缓存击穿
	 * @param value
	 */
	void setEnableBreakdownPrevent(boolean value);

	/**
	 * 获取防止缓存击穿的空值的缓存失效时间（秒）。
	 *
	 * @return
	 */
	int getBreakdownPreventExpiredSeconds();

	/**
	 * 设置防止缓存击穿的空值的缓存失效时间（秒），必须大于0。
	 *
	 * @param value
	 */
	void setBreakdownPreventExpiredSeconds(int value);

	/**
	 * 获取是否开启当命中缓存时延长过期时间
	 * @return
	 */
	boolean isEnableExtendExpiredSecondsWhenHit();

	/**
	 * 设置是否开启当命中缓存时延长过期时间
	 *
	 * @param value
	 */
	void setEnableExtendExpiredSecondsWhenHit(boolean value);

	/**
	 * 获取缓存的访问提供对象
	 *
	 * @return
	 */
	CacheProvider<K, V> getCacheProvider();
	
	/**
	 * 添加数据到缓存
	 * 
	 * @param key 	缓存键
	 * @param value 数据
	 */
	void add(K key, V value);

	/**
	 * 添加数据到缓存
	 *
	 * @param key 	缓存键
	 * @param value 数据
	 * @param expiredSeconds 过期秒值
	 */
	void add(K key, V value, int expiredSeconds);

	/**
	 * 添加数据到缓存
	 *
	 * @param map 键-值数据
	 */
	void addAll(Map<K, V> map);

	/**
	 * 添加数据到缓存
	 *
	 * @param map 键-值数据
	 * @param expiredSeconds 过期秒值
	 */
	void addAll(Map<K, V> map, int expiredSeconds);

	/**
	 * 从缓存获取数据
	 *
	 * @param key 	缓存键
	 * @return 		数据
	 */
	V get(K key);

	/**
	 * 从缓存获取数据
	 *
	 * @param collection 		缓存键
	 * @return 数据
	 */
	List<V> getAll(Collection<K> collection);
	
	/**
	 * 从缓存获取数据
	 *
	 * @param collection 		缓存键
	 * @return 数据
	 */
	Map<K, V> getMap(Collection<K> collection);

	/**
	 * 从缓存获取数据；若缓存中没有数据，则从指定的代理来获取数据，并更新缓存。
	 *
	 * @param key 		缓存键
	 * @param fetcher 	数据获取的代理（从数据库获取数据）
	 * @return 数据
	 */
	V getAndFetch(K key, Function<K, V> fetcher);

	/**
	 * 从缓存获取数据；若缓存中没有数据，则从指定的代理来获取数据，并更新缓存。
	 *
	 * @param collection	缓存键
	 * @param fetcher		数据获取的代理（从数据库获取数据）
	 * @param multiFetcher	数据获取的代理（从数据库获取数据），用于一次从数据库获取多条记录
	 * @return
	 */
	List<V> getAllAndFetch(Collection<K> collection,
                           Function<K, V> fetcher,
                           Supplier<List<V>> multiFetcher);

	/**
	 * 从缓存获取数据；若缓存中没有数据，则从指定的代理来获取数据，并更新缓存。
	 *
	 * @param keyCollection		缓存键
	 * @param delegate			数据获取的代理（从数据库获取数据）
	 * @return
	 */
	List<V> getAllAndFetch(Collection<K> keyCollection,
                           Function<Collection<K>, Map<K, V>> delegate);

	/**
	 * 从缓存获取数据；若缓存中没有数据，则从指定的代理来获取数据，并更新缓存。
	 *
	 * @param collection	缓存键
	 * @param fetcher		数据获取的代理（从数据库获取数据）
	 * @param multiFetcher	数据获取的代理（从数据库获取数据），用于一次从数据库获取多条记录
	 * @return
	 */
	Map<K, V> getMapAndFetch(Collection<K> collection,
                             Function<K, V> fetcher,
                             Supplier<Map<K, V>> multiFetcher);
	
	/**
	 * 从缓存删除数据
	 * 
	 * @param key 缓存键
	 * @return
	 */
	Boolean remove(K key);
	
	/**
	 * 从缓存删除数据
	 * 
	 * @param collection 缓存键
	 * @return
	 */
	Long removeAll(Collection<K> collection);
	
	/**
	 * 获取缓存剩余的时间（秒）
	 * 
	 * @param key 缓存键
	 * @return
	 */
	Long getExpire(K key);

	/**
	 * 获取所有的键
	 *
	 * @param pattern   匹配的字符，左匹配
	 * @param scanSize 扫描的记录数
	 * @return
	 */
	Set<K> getKeys(K pattern, int scanSize);
	
}
