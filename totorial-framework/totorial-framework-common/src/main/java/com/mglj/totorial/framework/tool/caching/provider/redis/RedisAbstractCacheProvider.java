package com.mglj.totorial.framework.tool.caching.provider.redis;


import com.mglj.totorial.framework.common.lang.CacheObject;
import com.mglj.totorial.framework.tool.caching.provider.CacheProvider;
import com.mglj.totorial.framework.tool.concurrent.locks.SimpleDisLock;
import com.mglj.totorial.framework.tool.concurrent.locks.SimpleDisLockFactory;
import com.mglj.totorial.framework.tool.data.redis.StringOperationTemplate;
import com.mglj.totorial.framework.tool.data.redis.locks.RedisSimpleDisLockToken;
import com.mglj.totorial.framework.tool.data.redis.operation.Operations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zsp on 2018/8/10.
 */
public abstract class RedisAbstractCacheProvider<K, V> implements CacheProvider<K, V> {

    protected final StringOperationTemplate<K, String> defaultTemplate;
    private final SimpleDisLockFactory simpleDisLockFactory;
    private final RedisSimpleDisLockToken redisSimpleDisLockToken;

    protected RedisAbstractCacheProvider(Class<K> keyType,
                                         Class<V> valueType,
                                         StringRedisTemplate stringRedisTemplate,
                                         String namespace,
                                         String domain,
                                         Operations operations,
                                         SimpleDisLockFactory simpleDisLockFactory) {
        this.defaultTemplate = new StringOperationTemplate(keyType,
                String.class,
                stringRedisTemplate,
                namespace,
                domain,
                operations);
        this.simpleDisLockFactory = simpleDisLockFactory;
        this.redisSimpleDisLockToken = new RedisSimpleDisLockToken(namespace, domain);
    }

    @Override
    public void setMultiOperationBatchSize(int value) {
        defaultTemplate.setMultiOperationBatchSize(value);
    }

    @Override
    public void setEnableKeyExpiredVolatility(boolean value) {
        defaultTemplate.setEnableKeyExpiredVolatility(value);
    }

    @Override
    public SimpleDisLock<String> getSimpleDisLock() {
        return simpleDisLockFactory.getSimpleDisLock(redisSimpleDisLockToken);
    }

    @Override
    public abstract void addToCache(K key, V value, int expiredSeconds);

    @Override
    public abstract void addToCache(Map<K, V> values, int expiredSeconds);

    @Override
    public void addEmptyToCache(K key, int expiredSeconds) {
        defaultTemplate.set(key, CacheObject.EMPTY_VALUE_FOR_BREAKDOWN_PREVENT, expiredSeconds);
    }

    @Override
    public abstract CacheObject<K, V> getFromCache(K key);

    @Override
    public abstract List<CacheObject<K, V>> getFromCache(Collection<K> keys);

    @Override
    public void expire(K key, int seconds) {
        defaultTemplate.expire(key, seconds);
    }

    @Override
    public void expire(Collection<K> keys, int seconds) {
        defaultTemplate.expire(keys, seconds);
    }

    @Override
    public Boolean removeFromCache(K key) {
        return defaultTemplate.delete(key);
    }

    @Override
    public Long removeFromCache(Collection<K> keys) {
        return defaultTemplate.delete(keys);
    }

    @Override
    public Long getExpire(K key) {
        return defaultTemplate.getExpire(key);
    }

    @Override
    public Set<K> getKeys(K pattern, int scanSize) {
        return defaultTemplate.getKeys(pattern, scanSize);
    }

    @Override
    public void clear() {

    }

}
