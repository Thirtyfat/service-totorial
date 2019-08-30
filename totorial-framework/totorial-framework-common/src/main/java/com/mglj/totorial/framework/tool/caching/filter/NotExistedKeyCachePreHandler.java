package com.mglj.totorial.framework.tool.caching.filter;

import java.util.Collection;

/**
 * Created by zsp on 2018/8/17.
 */
public class NotExistedKeyCachePreHandler implements CachePreHandler {
    @Override
    public <K> PreHandleResult isAllowed(K key) {
        return new PreHandleResult();
    }

    @Override
    public <K> PreHandleResult isAllowed(Collection<K> keys) {
        return new PreHandleResult();
    }
}
