package com.mglj.totorial.framework.tool.caching;


import com.mglj.totorial.framework.tool.caching.filter.CachePreHandler;
import com.mglj.totorial.framework.tool.caching.provider.CacheProvider;

/**
 * 缓存工厂
 *
 * Created by zsp on 2018/8/10.
 */
public interface CacheFactory {

    /**
     * 返回缓存预处理对象
     *
     * @return
     */
    CachePreHandler getCachePreHandler();

    /**
     * 构建一个缓存对象
     *
     * @param cacheManager      缓存管理对象
     * @param cacheProvider     缓存提供实现对象
     * @param <K>
     * @param <V>
     * @return
     */
    <K, V> Cache<K, V> create(CacheManager cacheManager,
                              CacheProvider<K, V> cacheProvider);

}
