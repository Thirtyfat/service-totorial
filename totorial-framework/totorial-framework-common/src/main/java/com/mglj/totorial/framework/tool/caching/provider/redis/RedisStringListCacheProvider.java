package com.mglj.totorial.framework.tool.caching.provider.redis;


import com.mglj.totorial.framework.common.lang.CacheObject;
import com.mglj.totorial.framework.tool.concurrent.locks.SimpleDisLockFactory;
import com.mglj.totorial.framework.tool.data.redis.StringListOperationTemplate;
import com.mglj.totorial.framework.tool.data.redis.operation.Operations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by zsp on 2018/8/20.
 */
public class RedisStringListCacheProvider<K, V extends List, E> extends RedisStringCacheProvider<K, V> {

    private StringListOperationTemplate<K, V, E> stringListOperationTemplate;

    public RedisStringListCacheProvider(Class<K> keyType,
                                        Class<V> valueType,
                                        Class<E> elementType,
                                        StringRedisTemplate stringRedisTemplate,
                                        String namespace,
                                        String domain,
                                        Operations operations,
                                        SimpleDisLockFactory simpleDisLockFactory) {
        super(keyType, valueType, stringRedisTemplate, namespace, domain, operations, simpleDisLockFactory);
        stringListOperationTemplate = new StringListOperationTemplate(keyType, valueType, elementType,
                stringRedisTemplate, namespace, domain, operations);
    }

    @Override
    public void setMultiOperationBatchSize(int value) {
        super.setMultiOperationBatchSize(value);
        stringListOperationTemplate.setMultiOperationBatchSize(value);
    }

    @Override
    public void setEnableKeyExpiredVolatility(boolean value) {
        super.setEnableKeyExpiredVolatility(value);
        stringListOperationTemplate.setEnableKeyExpiredVolatility(value);
    }

    public void setEnableMultiBucket(boolean enableMultiBucket) {
        stringListOperationTemplate.setEnableMultiBucket(enableMultiBucket);
    }

    public void setBucketItemSize(int bucketItemSize) {
        stringListOperationTemplate.setBucketItemSize(bucketItemSize);
    }

    @Override
    public void expire(K key, int seconds) {
        stringListOperationTemplate.expire(key, seconds);
    }

    @Override
    public void expire(Collection<K> keys, int seconds) {
        stringListOperationTemplate.expire(keys, seconds);
    }

    @Override
    public Boolean removeFromCache(K key) {
        return stringListOperationTemplate.delete(key);
    }

    @Override
    public Long removeFromCache(Collection<K> keys) {
        return stringListOperationTemplate.delete(keys);
    }

    @Override
    public Long getExpire(K key) {
        return stringListOperationTemplate.getExpire(key);
    }

    @Override
    public void addToCache(K key, V value, int expiredSeconds) {
        if(expiredSeconds > 0) {
            stringListOperationTemplate.set(key, value, expiredSeconds);
        } else {
            stringListOperationTemplate.set(key, value);
        }
    }

    @Override
    public void addToCache(Map<K, V> values, int expiredSeconds) {
        if(expiredSeconds > 0) {
            stringListOperationTemplate.multiSet(values, expiredSeconds);
        } else {
            stringListOperationTemplate.multiSet(values);
        }
    }

    @Override
    public CacheObject<K, V> getFromCache(K key) {
        return stringListOperationTemplate.getCacheObject(key);
    }

    @Override
    public List<CacheObject<K, V>> getFromCache(Collection<K> keys) {
        return stringListOperationTemplate.multiGetCacheObject(keys);
    }

}
