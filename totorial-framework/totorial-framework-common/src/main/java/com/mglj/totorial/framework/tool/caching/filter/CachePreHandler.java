package com.mglj.totorial.framework.tool.caching.filter;

import java.util.Collection;

/**
 * 缓存预处理操作
 *
 * Created by zsp on 2018/8/10.
 */
public interface CachePreHandler {

    /**
     * 是否允许键的缓存操作
     *
     * @param key
     * @return
     */
    <K> PreHandleResult isAllowed(K key);

    /**
     * 是否允许键的缓存操作
     *
     * @param keys
     * @return
     */
    <K> PreHandleResult isAllowed(Collection<K> keys);

}
