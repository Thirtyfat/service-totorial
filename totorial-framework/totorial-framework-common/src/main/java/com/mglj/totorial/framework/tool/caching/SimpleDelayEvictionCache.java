package com.mglj.totorial.framework.tool.caching;


import com.mglj.totorial.framework.tool.caching.provider.CacheProvider;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * 定义延迟删除缓存的策略
 * 
 * @author zsp
 *
 * @param <K>
 */
public final class SimpleDelayEvictionCache<K, V>
		extends SimpleCache<K, V> implements DelayEvictionCache<K, V> {

	 /**
     * 延迟删除缓存的时间（毫秒）
     */
    private int delayEvictMillis = 1000;
    
    private final DelayEvictionQueue delayEvictionQueue;
    
    public SimpleDelayEvictionCache(CacheManager cacheManager,
                                    CacheProvider<K, V> cacheProvider,
                                    CacheFactory cacheFactory,
                                    DelayEvictionQueue delayEvictionQueue) {
        super(cacheManager, cacheProvider, cacheFactory);
        this.delayEvictionQueue = delayEvictionQueue;
    }
    
    @Override
    public final void delayRemove(K key, Runnable updater) {
        cacheProvider.removeFromCache(key);
        if(updater != null) {
        	updater.run();
        }
        delayEvictionQueue.evict(this, key);
    }
    
    @Override
    public final <R> R delayRemove(K key, Supplier<R> updater) {
        cacheProvider.removeFromCache(key);
    	R result = null;
    	if(updater != null) {
    		result = updater.get();
    	}
        delayEvictionQueue.evict(this, key);
    	return result;
    }
    
    @Override
    public void delayRemove(K key) {
        delayEvictionQueue.evict(this, key);
    }
    
    @Override
    public void delayRemove(Collection<K> keys) {
    	if (keys != null && keys.size() > 0) {
    		for(K key : keys) {
                delayEvictionQueue.evict(this, key);
    		}
	    }
    }
    
    @Override
    public int getDelayEvictMillis() {
		return delayEvictMillis;
	}

    @Override
	public void setDelayEvictMillis(int delayEvictMillis) {
		this.delayEvictMillis = delayEvictMillis;
	}
	
}
