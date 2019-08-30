package com.mglj.totorial.framework.tool.caching.provider;


import com.mglj.totorial.framework.common.lang.CacheObject;
import com.mglj.totorial.framework.tool.concurrent.locks.SimpleDisLock;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zsp on 2018/8/10.
 */
public interface CacheProvider<K, V> {

    /**
     * 设置批量操作的记录数
     *
     * @param value
     */
    void setMultiOperationBatchSize(int value);

    /**
     * 设置开启键过期时间的上下浮动特性
     *
     * @return
     */
    void setEnableKeyExpiredVolatility(boolean value);

    /**
     * 返回一个分布式锁。
     *
     * @return
     */
    SimpleDisLock<String> getSimpleDisLock();

    /**
     * 添加数据到缓存，留给子类实现
     *
     * @param key   缓存键
     * @param value 数据
     * @param expiredSeconds
     */
    void addToCache(K key, V value, int expiredSeconds);

    /**
     * 添加数据到缓存，留给子类实现
     *
     * @param values 数据
     * @param expiredSeconds
     */
    void addToCache(Map<K, V> values, int expiredSeconds);

    /**
     * 添加空值，防止缓存击穿，留给子类实现
     *
     * @param key
     * @param expiredSeconds
     */
    void addEmptyToCache(K key, int expiredSeconds);

    /**
     * 从缓存获取数据，留给子类实现
     *
     * @param key 缓存键
     * @return 数据
     */
    CacheObject<K, V> getFromCache(K key);

    /**
     * 从缓存获取数据，留给子类实现
     *
     * @param keys 			缓存键
     * @return 				数据
     */
    List<CacheObject<K, V>> getFromCache(Collection<K> keys);

    /**
     * 延长缓存的过期时间（秒），留给子类实现
     *
     * @param key     缓存键
     * @param expiredSeconds 过期的时间（秒）
     */
    void expire(K key, int expiredSeconds);

    /**
     * 延长缓存的过期时间（秒），留给子类实现
     *
     * @param keys     缓存键
     * @param expiredSeconds 过期的时间（秒）
     */
    void expire(Collection<K> keys, int expiredSeconds);

    /**
     * 从缓存删除数据，留给子类实现
     *
     * @param key 缓存键
     * @return
     */
    Boolean removeFromCache(K key);

    /**
     * 从缓存删除数据，留给子类实现
     *
     * @param keys 缓存键
     * @return
     */
    Long removeFromCache(Collection<K> keys);

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

    /**
     * 清空缓存
     */
    void clear();

}
