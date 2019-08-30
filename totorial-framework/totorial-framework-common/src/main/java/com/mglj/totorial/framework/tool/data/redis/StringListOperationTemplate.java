package com.mglj.totorial.framework.tool.data.redis;

import com.mglj.totorial.framework.common.lang.CacheObject;
import com.mglj.totorial.framework.common.util.CollectionUtilsEx;
import com.mglj.totorial.framework.tool.converter.JsonListConverter;
import com.mglj.totorial.framework.tool.converter.StringConverter;
import com.mglj.totorial.framework.tool.data.redis.operation.Operations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.concurrent.TimeUnit;


import static com.mglj.totorial.framework.tool.data.redis.RedisConstants.REDIS_KEY_WORD_HYPHEN;
import static com.mglj.totorial.framework.tool.data.redis.RedisOperationEnum.*;


/**
 * 访问redis的模板工具，List对象以String类型存储，例如json格式，分批次的存储在多个（至少一个）的桶里。
 *
 * Created by zsp on 2018/8/9.
 */
public class StringListOperationTemplate<K, V extends List, E> extends StringOperationTemplate<K, V> {

    /**
     * 是否开启多个桶的存储方式
     */
    private boolean enableMultiBucket;
    private int bucketItemSize = 100;

    public StringListOperationTemplate(Class<K> keyType,
                                       Class<V> valueType,
                                       Class<E> elementType,
                                       StringRedisTemplate stringRedisTemplate,
                                       String namespace,
                                       String domain,
                                       Operations operations) {
        this(keyType, valueType, stringRedisTemplate, namespace, domain, operations,
                new JsonListConverter<>(elementType));
    }

    public StringListOperationTemplate(Class<K> keyType,
                                       Class<V> valueType,
                                       StringRedisTemplate stringRedisTemplate,
                                       String namespace,
                                       String domain,
                                       Operations operations,
                                       StringConverter<V> converter) {
        super(keyType, valueType, stringRedisTemplate, namespace, domain, operations,
                converter);
    }

    @Override
    public Boolean delete(K key) {
        validateKey(key);
        String wrapKey = wrapKey(key);
        if(enableMultiBucket) {
            return CollectionUtilsEx.batchForeach(
                    (loop) -> {
                        String key0 = wrapKey + REDIS_KEY_WORD_HYPHEN + String.valueOf(loop);
                        return deleteStringByLoop(key0, loop);
                    });
        }
        return deleteString(wrapKey);
    }

    @Override
    public Long delete(Collection<K> keys) {
        validateKey(keys);
        if(enableMultiBucket) {
            long deleted = 0;
            boolean affected;
            for (K key : keys) {
                String wrapKey = wrapKey(key);
                affected = CollectionUtilsEx.batchForeach(
                        (loop) -> {
                            String key0 = wrapKey + REDIS_KEY_WORD_HYPHEN + String.valueOf(loop);
                            return deleteStringByLoop(key0, loop);
                        });
                if (affected) {
                    deleted++;
                }
            }
            return deleted;
        }
        return deleteString(wrapKey(keys));
    }

    private boolean deleteStringByLoop(String key, int loop) {
        return operations.execute(DEL, getKeyPrefix(), Arrays.asList(key),
                () -> {
                    return stringRedisTemplate.delete(key);
                }, loop);
    }

    @Override
    public Boolean hasKey(K key) {
        validateKey(key);
        String key0 = wrapKey(key);
        if(enableMultiBucket) {
            key0 = key0 + REDIS_KEY_WORD_HYPHEN + "0";
        }
        return hasStringKey(key0);
    }

    @Override
    public Boolean expire(K key, long seconds) {
        if(seconds < 1) {
            return false;
        }
        validateKey(key);
        String wrapKey = wrapKey(key);
        if(enableMultiBucket) {
            return CollectionUtilsEx.batchForeach(
                    (loop) -> {
                        String key0 = wrapKey + REDIS_KEY_WORD_HYPHEN + String.valueOf(loop);
                        return expireStringByLoop(key0, seconds, loop);
                    });
        }
        return expireString(wrapKey, seconds);
    }

    @Override
    public void expire(Collection<K> keys, long seconds) {
        if(seconds < 1) {
            return;
        }
        validateKey(keys);
        if(enableMultiBucket) {
            for (K key : keys) {
                String wrapKey = wrapKey(key);
                CollectionUtilsEx.batchForeach(
                        (loop) -> {
                            String key0 = wrapKey + REDIS_KEY_WORD_HYPHEN + String.valueOf(loop);
                            return expireStringByLoop(key0, seconds, loop);
                        });
            }
        } else {
            expireString(wrapKey(keys), seconds);
        }
    }

    private boolean expireStringByLoop(String key, long seconds, int loop) {
        return operations.execute(EXPIRE, getKeyPrefix(), Arrays.asList(key),
                () -> {
                    return stringRedisTemplate.expire(key,
                            seconds + getExpiredSecondsVolatility(seconds),
                            TimeUnit.SECONDS);
                }, loop);
    }

    @Override
    public Boolean expireAt(K key, Date date) {
        throw new NotImplementedException();
    }

    @Override
    public void expireAt(Collection<K> keys, Date date) {
        throw new NotImplementedException();
    }

    @Override
    public Long getExpire(K key) {
        validateKey(key);
        String key0 = wrapKey(key);
        if(enableMultiBucket) {
            key0 = key0 + REDIS_KEY_WORD_HYPHEN + 0;
        }
        return getExpireString(key0);
    }

    @Override
    public void set(K key, V value, long seconds) {
        validateKey(key);
        if(enableMultiBucket) {
            List<E> list = value;
            if (list.size() > 0) {
                Map<String, String> map = new HashMap<>();
                CollectionUtilsEx.foreachSubList(list, bucketItemSize,
                        (loop, startIndex, endIndex, subList) -> {
                            String key0 = wrapKey(key) + REDIS_KEY_WORD_HYPHEN + String.valueOf(loop);
                            String value0 = converter.serialize((V) subList);
                            map.put(key0, value0);
                        });
                multiSetString(map, seconds);
            }
        } else {
            String value0 = converter.serialize(value);
            setString(wrapKey(key), value0, seconds);
        }
    }

    @Override
    public boolean setIfAbsent(K key, V value) {
        throw new NotImplementedException();
    }

    @Override
    public void multiSet(Map<K, V> map, long seconds) {
        validateKey(map.keySet());
        if(enableMultiBucket) {
            int batchSize = bucketItemSize;
            Map<String, String> map0 = new HashMap<>();
            for (Map.Entry<K, V> entry : map.entrySet()) {
                String wrapKey = wrapKey(entry.getKey());
                List<E> list = entry.getValue();
                if (list.size() > 0) {
                    CollectionUtilsEx.foreachSubList(list, batchSize,
                            (loop, startIndex, endIndex, subList) -> {
                                String key0 = wrapKey + REDIS_KEY_WORD_HYPHEN + String.valueOf(loop);
                                String value0 = converter.serialize((V) subList);
                                map0.put(key0, value0);
                            });
                }
            }
            multiSetString(map0, seconds);
        } else {
            multiSetString(toStringMap(map), seconds);
        }
    }

    @Override
    public V get(K key) {
        validateKey(key);
        if(enableMultiBucket) {
            V list = (V) CollectionUtilsEx.batchFetchList(bucketItemSize,
                    (loop) -> {
                        return getByLoop(key, loop);
                    });
            if (!CollectionUtils.isEmpty(list)) {
                return list;
            } else {
                return null;
            }
        }
        return getString(wrapKey(key));
    }

    @Override
    public List<V> multiGet(Collection<K> keys) {
        validateKey(keys);
        if(enableMultiBucket) {
            List<V> list = new ArrayList<>();
            int batchSize = bucketItemSize;
            for (K key : keys) {
                V list0 = (V) CollectionUtilsEx.batchFetchList(batchSize,
                        (loop) -> {
                            return getByLoop(key, loop);
                        });
                if (!CollectionUtils.isEmpty(list0)) {
                    list.add(list0);
                }
            }
            return list;
        }
        return multiGetString(wrapKey(keys));
    }

    private List<E> getByLoop(K key, int loop) {
        String key0 = wrapKey(key) + REDIS_KEY_WORD_HYPHEN + String.valueOf(loop);
        return operations.execute(GET, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return converter.deserialize(valueOps.get(key0), valueType);
                }, loop);
    }

    @Override
    public CacheObject<K, V> getCacheObject(K key) {
        validateKey(key);
        if(enableMultiBucket) {
            List<E> list = new ArrayList<>();
            List<E> subList;
            for(int loop = 0; ;loop++) {
                CacheObject<K, V> cacheObject = getCacheObjectByLoop(key, loop);
                if(cacheObject.isEmpty()) {
                    return cacheObject;
                }
                subList = cacheObject.getValue();
                if(!CollectionUtils.isEmpty(subList)) {
                    list.addAll(subList);
                }
                if(CollectionUtils.isEmpty(subList) || subList.size() < bucketItemSize) {
                    break;
                }
            }
            if (!CollectionUtils.isEmpty(list)) {
                return new CacheObject<K, V>(key, (V)list);
            } else {
                return new CacheObject<K, V>(key);
            }
        }
        return getCacheObjectString(key, wrapKey(key));
    }

    @Override
    public List<CacheObject<K, V>> multiGetCacheObject(Collection<K> keys) {
        validateKey(keys);
        if(enableMultiBucket) {
            List<CacheObject<K, V>> list = new ArrayList<>();
            int batchSize = bucketItemSize;
            for (K key : keys) {
                List<E> list0 = new ArrayList<>();
                List<E> subList;
                for(int loop = 0; ;loop++) {
                    CacheObject<K, V> cacheObject = getCacheObjectByLoop(key, loop);
                    if(cacheObject.isEmpty()) {
                        list.add(cacheObject);
                        break;
                    }
                    subList = cacheObject.getValue();
                    if(!CollectionUtils.isEmpty(subList)) {
                        list0.addAll(subList);
                    }
                    if(CollectionUtils.isEmpty(subList) || subList.size() < batchSize) {
                        break;
                    }
                }
                if (!CollectionUtils.isEmpty(list0)) {
                    list.add(new CacheObject<K, V>(key, (V)list0));
                }
            }
            return list;
        }
        return multiGetCacheObjectString(keys, wrapKey(keys));
    }

    private CacheObject<K, V> getCacheObjectByLoop(K key, int loop) {
        String key0 = wrapKey(key) + REDIS_KEY_WORD_HYPHEN + String.valueOf(loop);
        return operations.execute(GET, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return asCacheObject(key, valueOps.get(key0));
                }, loop);
    }

    @Override
    public V getAndSet(K key, V value) {
        throw new NotImplementedException();
    }

    public boolean isEnableMultiBucket() {
        return enableMultiBucket;
    }

    public void setEnableMultiBucket(boolean enableMultiBucket) {
        this.enableMultiBucket = enableMultiBucket;
    }

    public int getBucketItemSize() {
        return bucketItemSize;
    }

    public void setBucketItemSize(int bucketItemSize) {
        this.bucketItemSize = bucketItemSize;
    }
}
