package com.mglj.totorial.framework.tool.data.redis;

import com.mglj.totorial.framework.tool.converter.JsonListConverter;
import com.mglj.totorial.framework.tool.converter.StringConverter;
import com.mglj.totorial.framework.tool.data.redis.operation.Operations;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;
import static com.mglj.totorial.framework.tool.data.redis.RedisOperationEnum.*;

/**
 * Created by lishuaishuai on 2018/10/20.
 */
public class HashListOperationTemplate<K, F, V extends List, E> extends AbstractOperationTemplate<K, V> {

    private final HashOperations<String, String, String> hashOps;

    public HashListOperationTemplate(Class<K> keyType,
                                     Class<F> fieldType,
                                     Class<V> valueType,
                                     Class<E> elementType,
                                     StringRedisTemplate stringRedisTemplate,
                                     String namespace,
                                     String domain,
                                     Operations operations) {
        this(keyType, valueType, stringRedisTemplate, namespace, domain, operations,
                new JsonListConverter<>(elementType));
    }

    public HashListOperationTemplate(Class<K> keyType,
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
        String key0 = wrapKey(key);
        String value0 = converter.serialize(value);
        operations.execute(HSET, getKeyPrefix(), Arrays.asList(key0), () -> {
            hashOps.put(key0, String.valueOf(field), value0);
        }, field, value0);
    }

    public Boolean putIfAbsent(K key, F field, V value) {
        validateKey(key);
        String key0 = wrapKey(key);
        String value0 = converter.serialize(value);
        return operations.execute(HSETNX, getKeyPrefix(), Arrays.asList(key0), () -> {
            return hashOps.putIfAbsent(key0, String.valueOf(field), value0);
        }, field, value0);
    }

    public void putAll(K key, Map<F, V> map) {
        if (CollectionUtils.isEmpty(map)) {
            return;
        }
        validateKey(key);
        String key0 = wrapKey(key);
        Map<String, String> map0 = new HashMap<>();
        for (Map.Entry<F, V> entry : map.entrySet()) {
            map0.put(String.valueOf(entry.getKey()), converter.serialize(entry.getValue()));
        }
        operations.execute(HMSET, getKeyPrefix(), Arrays.asList(key0), () -> {
            hashOps.putAll(key0, map0);
        }, map);
    }

    public V get(K key, F field) {
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(HGET, getKeyPrefix(), Arrays.asList(key0), () -> {
            return converter.deserialize(hashOps.get(key0, String.valueOf(field)), valueType);
        }, field);
    }

    public List<V> multiGet(K key, Collection<F> fields) {
        validateKey(key);
        String key0 = wrapKey(key);
        Collection<String> fields0 = new HashSet<>();
        fields.forEach(f -> {
            fields0.add(String.valueOf(f));
        });
        List<String> stringValueList = operations.execute(HMGET, getKeyPrefix(), Arrays.asList(key0), () -> {
            return hashOps.multiGet(key0, fields0);
        }, fields);
        List<V> valueList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(stringValueList)) {
            for (String stringValue : stringValueList) {
                valueList.add(converter.deserialize(stringValue, valueType));
            }
        }
        return valueList;
    }

    public Long delete(K key, F... fields) {
        validateKey(key);
        String key0 = wrapKey(key);
        List<String> fields0 = new ArrayList<>();
        for (F field : fields) {
            fields0.add(String.valueOf(field));
        }
        return operations.execute(HDEL, getKeyPrefix(), Arrays.asList(key0), () -> {
            return hashOps.delete(key0, fields0);
        }, fields0);
    }

    public Boolean hasField(K key, F field) {
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(HEXISTS, getKeyPrefix(), Arrays.asList(key0), () -> {
            return hashOps.hasKey(key0, String.valueOf(field));
        }, field);
    }

    public Set<F> fields(K key) {
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(HKEYS, getKeyPrefix(), Arrays.asList(key0), () -> {
            Set<String> resultStr = hashOps.keys(key0);
            Set<F> resuslt = new HashSet<>();
            for (String item : resultStr) {
                resuslt.add((F) item);
            }
            return resuslt;
        });
    }

    public List<V> values(K key) {
        validateKey(key);
        String key0 = wrapKey(key);
        List<String> stringValueList = operations.execute(HVALS, getKeyPrefix(), Arrays.asList(key0), () -> {
            return hashOps.values(key0);
        });
        List<V> valueList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(stringValueList)) {
            for (String stringValue : stringValueList) {
                valueList.add(converter.deserialize(stringValue, valueType));
            }
        }
        return valueList;
    }

    public Map<F, V> entries(K key) {
        validateKey(key);
        String key0 = wrapKey(key);
        Map<String, String> stringValueMap = operations.execute(HGETALL, getKeyPrefix(), Arrays.asList(key0), () -> {
            return hashOps.entries(key0);
        });
        Map<F, V> valueMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(stringValueMap)) {
            for (Map.Entry<String, String> entry : stringValueMap.entrySet()) {
                valueMap.put((F) (entry.getKey()), converter.deserialize(entry.getValue(), valueType));
            }
        }
        return valueMap;
    }

    public Long size(K key) {
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(HLEN, getKeyPrefix(), Arrays.asList(key0), () -> {
            return hashOps.size(key0);
        });
    }

    public Long increment(K key, F field, long delta) {
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(HINCRBY, getKeyPrefix(), Arrays.asList(key0), () -> {
            return hashOps.increment(key0, String.valueOf(field), delta);
        }, field, delta);
    }

    public Double increment(K key, F field, double delta) {
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(HINCRBYFLOAT, getKeyPrefix(), Arrays.asList(key0), () -> {
            return hashOps.increment(key0, String.valueOf(field), delta);
        }, field, delta);
    }

    public Map<F, V> scan(K key, String pattern, int scanSize) {
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(HSCAN, getKeyPrefix(), Arrays.asList(key0), () -> {
            Map<F, V> valueMap = new HashMap<>();
            Cursor<Map.Entry<String, String>> cursor = hashOps.scan(key0, new ScanOptions.ScanOptionsBuilder()
                    .match(pattern.concat("*"))
                    .count(scanSize)
                    .build());
            Map.Entry<String, String> entry;
            while (cursor.hasNext()) {
                entry = cursor.next();
                valueMap.put((F) (entry.getKey()), converter.deserialize(entry.getValue(), valueType));
            }
            return valueMap;
        }, pattern, scanSize);
    }

}
