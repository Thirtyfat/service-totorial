package com.mglj.totorial.framework.tool.caching.provider.redis;


import com.mglj.totorial.framework.common.lang.CacheObject;
import com.mglj.totorial.framework.tool.concurrent.locks.SimpleDisLockFactory;
import com.mglj.totorial.framework.tool.data.redis.StringOperationTemplate;
import com.mglj.totorial.framework.tool.data.redis.operation.Operations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by zsp on 2018/8/10.
 */
public class RedisStringCacheProvider<K, V> extends RedisAbstractCacheProvider<K, V> {

    private StringOperationTemplate<K, V> stringOperationTemplate;

    public RedisStringCacheProvider(Class<K> keyType,
                                    Class<V> valueType,
                                    StringRedisTemplate stringRedisTemplate,
                                    String namespace,
                                    String domain,
                                    Operations operations,
                                    SimpleDisLockFactory simpleDisLockFactory) {
        super(keyType, valueType, stringRedisTemplate, namespace, domain, operations, simpleDisLockFactory);
        stringOperationTemplate = new StringOperationTemplate<>(keyType, valueType, stringRedisTemplate,
                namespace, domain, operations);
    }

    @Override
    public void setMultiOperationBatchSize(int value) {
        super.setMultiOperationBatchSize(value);
        stringOperationTemplate.setMultiOperationBatchSize(value);
    }

    @Override
    public void setEnableKeyExpiredVolatility(boolean value) {
        super.setEnableKeyExpiredVolatility(value);
        stringOperationTemplate.setEnableKeyExpiredVolatility(value);
    }

    @Override
    public void addToCache(K key, V value, int expiredSeconds) {
        if(expiredSeconds > 0) {
            stringOperationTemplate.set(key, value, expiredSeconds);
        } else {
            stringOperationTemplate.set(key, value);
        }
    }

    @Override
    public void addToCache(Map<K, V> values, int expiredSeconds) {
        if(expiredSeconds > 0) {
            stringOperationTemplate.multiSet(values, expiredSeconds);
        } else {
            stringOperationTemplate.multiSet(values);
        }
    }

    @Override
    public CacheObject<K, V> getFromCache(K key) {
        return stringOperationTemplate.getCacheObject(key);
    }

    @Override
    public List<CacheObject<K, V>> getFromCache(Collection<K> keys) {
        return stringOperationTemplate.multiGetCacheObject(keys);
    }

//    protected final List<CacheObject<K, V>> getFromCache(Collection<K> keys, Supplier<List<V>> multiGetter) {
//        List<V> values = multiGetter.get();
//        if(CollectionUtils.isEmpty(values) || values.size() != keys.size()) {
//            throw new CacheException("The values are not match the keys!");
//        }
//        List<CacheObject<K, V>> list = new ArrayList<CacheObject<K, V>>();
//        if(keyMapper != null) {
//            /*
//			 * 因为无法从null或空值中分析出缓存键的信息，因此只能先从缓存返回的结果构建已缓存的对象，
//			 * 然后从全部查询键里过滤掉已缓存的，即剩下的是“未缓存”的（包含空值的情况）。
//			 */
//            Set<K> cachedKeySet = new HashSet<>();
//            for(V value : values) {
//                if(value != null && !CacheObject.isEmptyValueForBreakdownPrevent(value)) {
//                    K key = keyMapper.apply(value);
//                    list.add(new CacheObject(key, value));
//                    cachedKeySet.add(key);
//                }
//            }
//            Set<K> uncachedKeySet = new HashSet<>(keys);
//            uncachedKeySet.removeAll(cachedKeySet);
//            if(uncachedKeySet.size() > 0) {
//                for(K key : uncachedKeySet) {
//                    list.add(new CacheObject<K, V>(key));
//                }
//            }
//        } else {
//            Iterator<V> it = values.iterator();
//            for(K key : keys) {
//                V value = it.next();
//                if(value != null && !CacheObject.isEmptyValueForBreakdownPrevent(value)) {
//                    list.add(new CacheObject(key, value));
//                } else {
//                    list.add(new CacheObject<K, V>(key));
//                }
//            }
//        }
//
//        return list;
//    }

}
