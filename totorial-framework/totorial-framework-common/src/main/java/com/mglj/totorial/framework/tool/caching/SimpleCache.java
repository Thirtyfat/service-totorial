package com.mglj.totorial.framework.tool.caching;


import com.mglj.totorial.framework.tool.caching.provider.CacheProvider;

/**
 * Created by zsp on 2018/7/22.
 */
public class SimpleCache<K, V> extends AbstractCache<K, V> {

    public SimpleCache(CacheManager cacheManager,
                       CacheProvider<K, V> cacheProvider,
                       CacheFactory cacheFactory) {
        super(cacheManager, cacheProvider, cacheFactory);
    }

}
