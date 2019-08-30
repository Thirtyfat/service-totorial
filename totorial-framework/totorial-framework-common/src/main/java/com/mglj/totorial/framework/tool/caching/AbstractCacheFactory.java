package com.mglj.totorial.framework.tool.caching;

import com.mglj.totorial.framework.tool.caching.filter.CachePreHandler;
import com.mglj.totorial.framework.tool.caching.provider.CacheProvider;
import com.mglj.totorial.framework.tool.concurrent.threading.ExecutorServiceFactory;
import com.mglj.totorial.framework.tool.concurrent.threading.FixedExecutorServiceFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by zsp on 2018/8/10.
 */
public abstract class AbstractCacheFactory implements CacheFactory, InitializingBean, DisposableBean {

    private final ExecutorServiceFactory asyncOperationThreadFactory;
    private final ExecutorService executorService;
    private final List<Cache> cacheList;

    private CachePreHandler cachePreHandler;

    public AbstractCacheFactory() {
        asyncOperationThreadFactory = new FixedExecutorServiceFactory("cache-async-operation", true);
        executorService = asyncOperationThreadFactory.get();
        cacheList = new CopyOnWriteArrayList<>();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    @Override
    public final void destroy() throws Exception {
        shutdown();
        asyncOperationThreadFactory.destroy();
        cacheList.clear();
    }

    @Override
    public final <K, V> Cache<K, V> create(CacheManager cacheManager,
                                           CacheProvider<K, V> cacheProvider) {
        Objects.requireNonNull(cacheProvider);
        Cache<K, V> cache = createCache(cacheManager, cacheProvider);
        if(cache != null) {
            cacheList.add(cache);
        }

        return cache;
    }

    protected final void asyncExecute(Runnable command) {
        executorService.execute(command);
    }

    protected final <R> Future<R> asyncSubmit(Callable<R> task) {
        return executorService.submit(task);
    }

    protected abstract <K, V> Cache<K, V> createCache(CacheManager cacheManager,
                                                      CacheProvider<K, V> cacheProvider);

    protected abstract void init();

    protected abstract void shutdown();

    List<Cache> getCacheList() {
        return cacheList;
    }

    @Override
    public CachePreHandler getCachePreHandler() {
        return cachePreHandler;
    }

    public void setCachePreHandler(CachePreHandler cachePreHandler) {
        this.cachePreHandler = cachePreHandler;
    }

}
