package com.mglj.totorial.framework.tool.caching;


import com.mglj.totorial.framework.tool.caching.provider.CacheProvider;
import com.mglj.totorial.framework.tool.concurrent.threading.ExecutorServiceFactory;
import com.mglj.totorial.framework.tool.concurrent.threading.FixedExecutorServiceFactory;

/**
 * Created by zsp on 2018/8/10.
 */
public class SimpleDelayEvictionCacheFactory extends AbstractCacheFactory {

    private final ExecutorServiceFactory delayEvictionThreadFactory;
    private final DelayEvictionQueue delayEvictionQueue;

    private int evictionWorkThreadSize = Runtime.getRuntime().availableProcessors() - 1;
    private int maxEvictionQueueSize = 1024;
    private int delayStopQueueMillis = 5000;

    public SimpleDelayEvictionCacheFactory() {
        super();
        delayEvictionThreadFactory = new FixedExecutorServiceFactory("cache-delay-eviction", true,
                evictionWorkThreadSize + 1, maxEvictionQueueSize);
        delayEvictionQueue = new DelayEvictionQueue(delayEvictionThreadFactory.get(), delayStopQueueMillis);
    }

    @Override
    protected <K, V> Cache<K, V> createCache(CacheManager cacheManager,
                                             CacheProvider<K, V> cacheProvider) {
        return new SimpleDelayEvictionCache(cacheManager, cacheProvider, this,
                delayEvictionQueue);
    }

    @Override
    protected void init() {
        delayEvictionQueue.start();
    }

    @Override
    protected void shutdown() {
        delayEvictionQueue.stop();
        delayEvictionThreadFactory.destroy();
    }

    public int getEvictionWorkThreadSize() {
        return evictionWorkThreadSize;
    }

    public void setEvictionWorkThreadSize(int evictionWorkThreadSize) {
        this.evictionWorkThreadSize = evictionWorkThreadSize;
    }

    public int getMaxEvictionQueueSize() {
        return maxEvictionQueueSize;
    }

    public void setMaxEvictionQueueSize(int maxEvictionQueueSize) {
        this.maxEvictionQueueSize = maxEvictionQueueSize;
    }

    public int getDelayStopQueueMillis() {
        return delayStopQueueMillis;
    }

    public void setDelayStopQueueMillis(int delayStopQueueMillis) {
        this.delayStopQueueMillis = delayStopQueueMillis;
    }

}
