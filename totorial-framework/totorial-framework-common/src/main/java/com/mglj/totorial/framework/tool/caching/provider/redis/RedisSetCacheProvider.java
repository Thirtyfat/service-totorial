package com.mglj.totorial.framework.tool.caching.provider.redis;


import com.mglj.totorial.framework.common.lang.CacheObject;
import com.mglj.totorial.framework.tool.concurrent.locks.SimpleDisLockFactory;
import com.mglj.totorial.framework.tool.data.redis.SetOperationTemplate;
import com.mglj.totorial.framework.tool.data.redis.operation.Operations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;

/**
 * Created by zsp on 2018/9/29.
 */
public class RedisSetCacheProvider<K, V extends Set<V>> extends RedisAbstractCacheProvider<K, V> {

    private SetOperationTemplate<K, V> setOperationTemplate;

    public RedisSetCacheProvider(Class<K> keyType,
                                    Class<V> valueType,
                                    StringRedisTemplate stringRedisTemplate,
                                    String namespace,
                                    String domain,
                                    Operations operations,
                                    SimpleDisLockFactory simpleDisLockFactory) {
        super(keyType, valueType, stringRedisTemplate, namespace, domain, operations, simpleDisLockFactory);
        setOperationTemplate = new SetOperationTemplate<>(keyType, valueType, stringRedisTemplate,
                namespace, domain, operations);
    }

    @Override
    public void setMultiOperationBatchSize(int value) {
        super.setMultiOperationBatchSize(value);
        setOperationTemplate.setMultiOperationBatchSize(value);
    }

    @Override
    public void setEnableKeyExpiredVolatility(boolean value) {
        super.setEnableKeyExpiredVolatility(value);
        setOperationTemplate.setEnableKeyExpiredVolatility(value);
    }

    @Override
    public void addToCache(K key, V value, int expiredSeconds) {
        setOperationTemplate.add(key, value);
        if(expiredSeconds > 0) {
            setOperationTemplate.expire(key, expiredSeconds);
        }
    }

    @Override
    public void addToCache(Map<K, V> values, int expiredSeconds) {
        for(Map.Entry<K, V> entry : values.entrySet()) {
            addToCache(entry.getKey(), entry.getValue(), expiredSeconds);
        }
    }

    @Override
    public CacheObject<K, V> getFromCache(K key) {
        return (CacheObject<K, V>)setOperationTemplate.membersCacheObject(key);
    }

    @Override
    public List<CacheObject<K, V>> getFromCache(Collection<K> keys) {
        List<CacheObject<K, V>> list = new ArrayList<>();
        for(K key : keys) {
            list.add(getFromCache(key));
        }
        return list;
    }
}
