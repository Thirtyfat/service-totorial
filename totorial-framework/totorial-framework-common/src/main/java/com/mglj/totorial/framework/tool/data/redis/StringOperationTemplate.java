package com.mglj.totorial.framework.tool.data.redis;

import com.mglj.totorial.framework.common.lang.CacheObject;
import com.mglj.totorial.framework.common.util.CollectionUtilsEx;
import com.mglj.totorial.framework.tool.converter.StringConverter;
import com.mglj.totorial.framework.tool.data.redis.operation.Operations;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import static com.mglj.totorial.framework.tool.data.redis.RedisOperationEnum.*;



/**
 * 访问redis的模板工具，对象以String类型存储，例如json格式。
 *
 * Created by zsp on 2018/8/9.
 */
public class StringOperationTemplate<K, V> extends AbstractOperationTemplate<K, V> {

    protected final ValueOperations<String, String> valueOps;

    public StringOperationTemplate(Class<K> keyType,
                                   Class<V> valueType,
                                   StringRedisTemplate stringRedisTemplate,
                                   String namespace,
                                   String domain,
                                   Operations operations) {
        super(keyType, valueType, stringRedisTemplate, namespace, domain, operations);
        this.valueOps = stringRedisTemplate.opsForValue();
    }

    public StringOperationTemplate(Class<K> keyType,
                                   Class<V> valueType,
                                   StringRedisTemplate stringRedisTemplate,
                                   String namespace,
                                   String domain,
                                   Operations operations,
                                   StringConverter<V> converter) {
        super(keyType, valueType, stringRedisTemplate, namespace, domain, operations, converter);
        this.valueOps = stringRedisTemplate.opsForValue();
    }

    public void set(K key, V value) {
        set(key, value, 0);
    }

    public void set(K key, V value, long seconds) {
        validateKey(key);
        String value0 = converter.serialize(value);
        setString(wrapKey(key), value0, seconds);
    }

    protected final void setString(String key, String value, long seconds) {
        operations.execute(SET, getKeyPrefix(), Arrays.asList(key),
                () -> {
                    if(seconds < 1) {
                        valueOps.set(key, value);
                    } else {
                        valueOps.set(key, value,
                                seconds + getExpiredSecondsVolatility(seconds),
                                TimeUnit.SECONDS);
                    }
                }, value, seconds);
    }

    public boolean setIfAbsent(K key, V value) {
        validateKey(key);
        String key0 = wrapKey(key);
        String value0 = converter.serialize(value);
        return operations.execute(SETNX, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return valueOps.setIfAbsent(key0, value0);
                }, value0);
    }

    public void multiSet(Map<K, V> map) {
        multiSet(map, 0);
    }

    public void multiSet(Map<K, V> map, long seconds) {
        validateKey(map.keySet());
        multiSetString(toStringMap(map), seconds);
    }

    protected final void multiSetString(Map<String, String> map, long seconds) {
        int batchSize = multiOperationBatchSize;
        if(map.size() > batchSize) {
            CollectionUtilsEx.foreachSubMap(map, batchSize,
                    (loop, startIndex, endIndex, subMap) -> {
                        multiSetString0(subMap, seconds);
                    });
        } else {
            multiSetString0(map, seconds);
        }
    }

    private void multiSetString0(Map<String, String> map, long seconds) {
        String keyPrefix = getKeyPrefix();
        operations.execute(MSET, keyPrefix, map.keySet(),
                () -> {
                    valueOps.multiSet(map);
                }, map);
        if(seconds > 0) {
            operations.execute(EXPIRE, keyPrefix, map.keySet(),
                    ()->{
                        return stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
                            @Nullable
                            @Override
                            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                                for(String key : map.keySet()) {
                                    connection.expire(keySerializer.serialize(key),
                                            seconds + getExpiredSecondsVolatility(seconds));
                                }
                                return null;
                            }
                        });
                    }, seconds);
        }
    }

    public V get(K key) {
        validateKey(key);
        return getString(wrapKey(key));
    }

    protected final V getString(String key) {
        return operations.execute(GET, getKeyPrefix(), Arrays.asList(key),
                () -> {
                    String value = valueOps.get(key);
                    return converter.deserialize(value, valueType);
                });
    }

    public List<V> multiGet(Collection<K> keys) {
        validateKey(keys);
        return multiGetString(wrapKey(keys));
    }

    protected final List<V> multiGetString(Collection<String> keys) {
        int batchSize = multiOperationBatchSize;
        if(keys.size() > batchSize) {
            List<V> list = new ArrayList<>();
            CollectionUtilsEx.foreachSubList(new ArrayList<>(keys), batchSize,
                    (loop, startIndex, endIndex, subList) -> {
                        list.addAll(multiGetString0(subList));
                    });
            return list;
        }
        return multiGetString0(keys);
    }

    private List<V> multiGetString0(Collection<String> keys) {
        List<String> list0 = operations.execute(MGET, getKeyPrefix(), keys,
                () -> {
                    return valueOps.multiGet(keys);
                });
        List<V> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list0)) {
            for(String item : list0) {
                list.add(converter.deserialize(item, valueType));
            }
        }
        return list;
    }

    public V getAndSet(K key, V value) {
        validateKey(key);
        String key0 = wrapKey(key);
        String value0 = converter.serialize(value);
        return operations.execute(GETSET, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return converter.deserialize(valueOps.getAndSet(key0, value0), valueType);
                }, value0);
    }

    public CacheObject<K, V> getCacheObject(K key) {
        validateKey(key);
        return getCacheObjectString(key, wrapKey(key));
    }

    protected final CacheObject<K, V> getCacheObjectString(K key, String wrapKey) {
        return operations.execute(GET, getKeyPrefix(), Arrays.asList(wrapKey),
                () -> {
                    return asCacheObject(key, valueOps.get(wrapKey));
                });
    }

    public List<CacheObject<K, V>> multiGetCacheObject(Collection<K> keys) {
        validateKey(keys);
        return multiGetCacheObjectString(keys, wrapKey(keys));
    }

    protected final List<CacheObject<K, V>> multiGetCacheObjectString(Collection<K> keys,
                                                                      Collection<String> wrapKeys) {
        int batchSize = multiOperationBatchSize;
        if(keys.size() > batchSize) {
            List<CacheObject<K, V>> list = new ArrayList<>();
            List<K> keyList = new ArrayList<>(keys);
            CollectionUtilsEx.foreachSubList(new ArrayList<>(wrapKeys), batchSize,
                    (loop, startIndex, endIndex, subList) -> {
                        list.addAll(multiGetCacheObjectString0(keyList.subList(startIndex, endIndex), subList));
                    });
            return list;
        }
        return multiGetCacheObjectString0(keys, wrapKeys);
    }

    private List<CacheObject<K, V>> multiGetCacheObjectString0(Collection<K> keys,
                                                               Collection<String> wrapKeys) {
        List<String> stringList = operations.execute(MGET, getKeyPrefix(), wrapKeys,
                () -> {
                    return valueOps.multiGet(wrapKeys);
                });
        List<CacheObject<K, V>> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(stringList)) {
            Iterator<K> it = keys.iterator();
            for(String value : stringList) {
                list.add(asCacheObject(it.next(), value));
            }
        }
        return list;
    }

    public long increment(K key, long delta) {
        return increment(key, delta, INCRBY);
    }

    public Map<K, Long> increment(Collection<K> collection, long delta) {
        return increment(collection, delta, INCRBY);
    }

    public double increment(K key, double delta) {
        return increment(key, delta, INCRBYFLOAT);
    }

    public Map<K, Double> increment(Collection<K> collection, double delta) {
        return increment(collection, delta, INCRBYFLOAT);
    }

    private <R> R increment(K key, R delta, RedisOperationEnum operation) {
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(operation, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    if(delta instanceof Long) {
                        return (R)valueOps.increment(key0, (Long) delta);
                    } else {
                        return (R)valueOps.increment(key0, (Double) delta);
                    }
                }, delta);
    }

    private <R> Map<K, R> increment(Collection<K> collection, R delta, RedisOperationEnum operation) {
        validateKey(collection);
        List<String> keyList = new ArrayList<>();
        for(K key : collection) {
            keyList.add(wrapKey(key));
        }
        List<Object> valueList = operations.execute(operation, getKeyPrefix(), keyList,
                ()->{
                    return stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
                        @Nullable
                        @Override
                        public Object doInRedis(RedisConnection connection) throws DataAccessException {
                            if(delta instanceof Long) {
                                for (String key : keyList) {
                                    connection.incrBy(keySerializer.serialize(key), (Long) delta);
                                }
                            } else {
                                for (String key : keyList) {
                                    connection.incrBy(keySerializer.serialize(key), (Double) delta);
                                }
                            }
                            return null;
                        }
                    });
                });
        Map<K, R> valueMap = new HashMap<>();
        Iterator<K> it = collection.iterator();
        for(int i = 0, len = collection.size(); i < len; i++) {
            valueMap.put(it.next(), (R)valueList.get(i));
        }
        return valueMap;
    }

    /**
     * Sets or clears the bit at offset in the string value stored at key.
     *
     * The bit is either set or cleared depending on value, which can be either 0 or 1.
     * When key does not exist, a new string value is created. The string is grown to make sure
     * it can hold a bit at offset. The offset argument is required to be greater than or equal
     * to 0, and smaller than 232 (this limits bitmaps to 512MB). When the string at key is grown,
     * added bits are set to 0.
     *
     * @param key
     * @param offset
     * @param value
     * @return
     */
    public boolean setBit(K key, long offset, boolean value) {
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(SETBIT, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return valueOps.setBit(key0, offset, value);
                }, offset, value);
    }

    /**
     * Returns the bit value at offset in the string value stored at key.
     *
     * When offset is beyond the string length, the string is assumed to be a contiguous space
     * with 0 bits. When key does not exist it is assumed to be an empty string, so offset is
     * always out of range and the value is also assumed to be a contiguous space with 0 bits.
     *
     * @param key
     * @param offset
     * @return      the bit value stored at offset.
     */
    public boolean getBit(K key, long offset) {
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(GETBIT, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return valueOps.getBit(key0, offset);
                }, offset);
    }

}
