package com.mglj.totorial.framework.tool.caching.filter;

import java.util.Collection;

/**
 * Created by zsp on 2018/8/17.
 */
public class ToggleEnableCachePreHandler implements CachePreHandler {

    private volatile boolean enabled;

    public ToggleEnableCachePreHandler() {

    }

    public ToggleEnableCachePreHandler(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public <K> PreHandleResult isAllowed(K key) {
        return enabled
                ? new PreHandleResult()
                : new PreHandleResult(PreHandleResultEnum.CACHE_DISABLED);
    }

    @Override
    public <K> PreHandleResult isAllowed(Collection<K> keys) {
        return enabled
                ? new PreHandleResult()
                : new PreHandleResult(PreHandleResultEnum.CACHE_DISABLED);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
