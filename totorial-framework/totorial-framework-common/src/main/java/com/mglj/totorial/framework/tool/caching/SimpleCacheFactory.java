package com.mglj.totorial.framework.tool.caching;


import com.mglj.totorial.framework.tool.caching.provider.CacheProvider;

/**
 * Created by zsp on 2018/8/10.
 */
public class SimpleCacheFactory extends AbstractCacheFactory {

    public SimpleCacheFactory() {
        super();
    }

    @Override
    protected <K, V> Cache<K, V> createCache(CacheManager cacheManager,
                                             CacheProvider<K, V> cacheProvider) {
        return new SimpleCache(cacheManager, cacheProvider, this);
    }

    @Override
    protected void init() {
        //do nothing.
    }

    @Override
    protected void shutdown() {
        //do nothing.
    }

}
