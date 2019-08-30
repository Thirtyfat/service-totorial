package com.mglj.totorial.framework.tool.data.redis;

import com.mglj.totorial.framework.common.lang.CacheObject;
import com.mglj.totorial.framework.tool.converter.StringConverter;
import com.mglj.totorial.framework.tool.data.redis.operation.Operations;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.mglj.totorial.framework.tool.data.redis.RedisOperationEnum.*;

/**
 * Created by zsp on 2018/8/9.
 */
public class HashOperationTemplate<K, F, V> extends AbstractOperationTemplate<K, V> {

    private final HashOperations<String, F, String> hashOps;

    public HashOperationTemplate(Class<K> keyType,
                                Class<V> valueType,
                                StringRedisTemplate stringRedisTemplate,
                                String namespace,
                                String domain,
                                Operations operations) {
        super(keyType, valueType, stringRedisTemplate, namespace, domain, operations);
        this.hashOps = stringRedisTemplate.opsForHash();
    }

    public HashOperationTemplate(Class<K> keyType,
                                 Class<V> valueType,
                                 StringRedisTemplate stringRedisTemplate,
                                 String namespace,
                                 String domain,
                                 Operations operations,
                                 StringConverter<V> converter) {
        super(keyType, valueType, stringRedisTemplate, namespace, domain, operations, converter);
        this.hashOps = stringRedisTemplate.opsForHash();
    }

    public void put(K key, F field, V value) {
        validateKey(key);
        validateField(field);
        String key0 = wrapKey(key);
        String value0 = converter.serialize(value);
        operations.execute(HSET, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    hashOps.put(key0, field, value0);
                }, field, value0);
    }

    public Boolean putIfAbsent(K key, F field, V value) {
        validateKey(key);
        validateField(field);
        String key0 = wrapKey(key);
        String value0 = converter.serialize(value);
        return operations.execute(HSETNX, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return hashOps.putIfAbsent(key0, field, value0);
                }, field, value0);
    }

    public void putAll(K key, Map<F, V> map) {
        if(CollectionUtils.isEmpty(map)) {
            return;
        }
        validateKey(key);
        String key0 = wrapKey(key);
        Map<F, String> map0 = new HashMap<>();
        F field;
        for(Map.Entry<F, V> entry : map.entrySet()) {
            field = entry.getKey();
            validateField(field);
            map0.put(field, converter.serialize(entry.getValue()));
        }
        operations.execute(HMSET, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    hashOps.putAll(key0, map0);
                }, map0);
    }

    public V get(K key, F field) {
        validateKey(key);
        validateField(field);
        String key0 = wrapKey(key);
        return operations.execute(HGET, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return converter.deserialize(hashOps.get(key0, field), valueType);
                }, field);
    }

    public List<V> multiGet(K key, Collection<F> fields) {
        if(CollectionUtils.isEmpty(fields)) {
            return new ArrayList<>();
        }
        validateKey(key);
        for(F field : fields) {
            validateField(field);
        }
        String key0 = wrapKey(key);
        List<String> stringValueList = operations.execute(HMGET, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return hashOps.multiGet(key0, fields);
                }, fields);
        List<V> valueList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(stringValueList)) {
            for(String stringValue : stringValueList) {
                valueList.add(converter.deserialize(stringValue, valueType));
            }
        }
        return valueList;
    }

    public Long delete(K key, F... fields) {
        if(fields == null || fields.length == 0) {
            return 0L;
        }
        validateKey(key);
        for(F field : fields) {
            validateField(field);
        }
        String key0 = wrapKey(key);
        return operations.execute(HDEL, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return hashOps.delete(key0, fields);
                }, fields);
    }

    public Boolean hasField(K key, F field) {
        validateKey(key);
        validateField(field);
        String key0 = wrapKey(key);
        return operations.execute(HEXISTS, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return hashOps.hasKey(key0, field);
                }, field);
    }

    public Set<F> fields(K key) {
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(HKEYS, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return hashOps.keys(key0);
                });
    }

    public List<V> values(K key) {
        validateKey(key);
        String key0 = wrapKey(key);
        List<String> stringValueList = operations.execute(HVALS, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return hashOps.values(key0);
                });
        List<V> valueList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(stringValueList)) {
            for(String stringValue : stringValueList) {
                valueList.add(converter.deserialize(stringValue, valueType));
            }
        }
        return valueList;
    }

    public Map<F, V> entries(K key) {
        validateKey(key);
        String key0 = wrapKey(key);
        Map<F, String> stringValueMap = operations.execute(HGETALL, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return hashOps.entries(key0);
                });
        Map<F, V> valueMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(stringValueMap)) {
            for(Map.Entry<F, String> entry : stringValueMap.entrySet()) {
                valueMap.put(entry.getKey(), converter.deserialize(entry.getValue(), valueType));
            }
        }
        return valueMap;
    }

    public CacheObject<K, Map<F, V>> entriesCacheObject(K key) {
        validateKey(key);
        String key0 = wrapKey(key);
        DataType dataType = operations.execute(TYPE, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return stringRedisTemplate.type(key0);
                });
        if(DataType.STRING.equals(dataType)) {
            return new CacheObject<K, Map<F, V>>(key).asEmpty();
        }
        Map<F, String> stringValueMap = operations.execute(HGETALL, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return hashOps.entries(key0);
                });
        Map<F, V> valueMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(stringValueMap)) {
            for(Map.Entry<F, String> entry : stringValueMap.entrySet()) {
                valueMap.put(entry.getKey(), converter.deserialize(entry.getValue(), valueType));
            }
        }
        return new CacheObject<K, Map<F, V>>(key, valueMap);
    }

    public Long size(K key) {
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(HLEN, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return hashOps.size(key0);
                });
    }

    public Long increment(K key, F field, long delta) {
        validateKey(key);
        validateField(field);
        String key0 = wrapKey(key);
        return operations.execute(HINCRBY, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return hashOps.increment(key0, field, delta);
                }, field, delta);
    }

    public Double increment(K key, F field, double delta) {
        validateKey(key);
        validateField(field);
        String key0 = wrapKey(key);
        return operations.execute(HINCRBYFLOAT, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return hashOps.increment(key0, field, delta);
                }, field, delta);
    }

    public Map<F, V> scan(K key, String pattern, int scanSize) {
        if(scanSize < 1) {
            throw new IllegalArgumentException("The scanSize should be than 0.");
        }
        validateKey(key);
        String key0 = wrapKey(key);
        String pattern0 = (pattern == null) ? "" : pattern;
        return operations.execute(HSCAN, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    Map<F, V> valueMap = new HashMap<>();
                    Cursor<Map.Entry<F, String>> cursor = hashOps.scan(key0,
                            new ScanOptions.ScanOptionsBuilder()
                                .match(pattern0.concat("*"))
                                .count(scanSize)
                                .build());
                    Map.Entry<F, String> entry;
                    while (cursor.hasNext()) {
                        entry = cursor.next();
                        valueMap.put(entry.getKey(), converter.deserialize(entry.getValue(), valueType));
                    }
                    return valueMap;
                }, pattern, scanSize);
    }

    private void validateField(F field) {
        Objects.requireNonNull(field, "The field is null.");
        KeyUtils.checkType(field.getClass());
    }

}
