package com.mglj.totorial.framework.tool.data.redis;

import com.mglj.totorial.framework.common.lang.CacheObject;
import com.mglj.totorial.framework.tool.converter.StringConverter;
import com.mglj.totorial.framework.tool.data.redis.operation.Operations;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.mglj.totorial.framework.tool.data.redis.RedisOperationEnum.*;


/**
 * Created by zsp on 2018/8/9.
 */
public class SetOperationTemplate<K, V> extends AbstractOperationTemplate<K, V> {

    private final SetOperations<String, String> setOps;

    public SetOperationTemplate(Class<K> keyType,
                                Class<V> valueType,
                                StringRedisTemplate stringRedisTemplate,
                                String namespace,
                                String domain,
                                Operations operations) {
        super(keyType, valueType, stringRedisTemplate, namespace, domain, operations);
        this.setOps = stringRedisTemplate.opsForSet();
    }

    public SetOperationTemplate(Class<K> keyType,
                                Class<V> valueType,
                                StringRedisTemplate stringRedisTemplate,
                                String namespace,
                                String domain,
                                Operations operations,
                                StringConverter<V> converter) {
        super(keyType, valueType, stringRedisTemplate, namespace, domain, operations, converter);
        this.setOps = stringRedisTemplate.opsForSet();
    }

    public Long add(K key, V... values) {
        if(values == null || values.length == 0) {
            return 0L;
        }
        validateKey(key);
        String key0 = wrapKey(key);
        String[] valueArray = toStringArray(values);
        return operations.execute(SADD, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return setOps.add(key0, valueArray);
                }, valueArray);
    }

    public Long size(K key) {
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(SCARD, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return setOps.size(key0);
                });
    }

    public Boolean isMember(K key, V value) {
        validateKey(key);
        String key0 = wrapKey(key);
        String value0 = converter.serialize(value);
        return operations.execute(SISMEMBER, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return setOps.isMember(key0, value0);
                }, value0);
    }

    public Set<V> members(K key) {
        validateKey(key);
        String key0 = wrapKey(key);
        Set<String> set0 = operations.execute(SMEMBERS, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return setOps.members(key0);
                });
        Set<V> set = new HashSet<>();
        if(!CollectionUtils.isEmpty(set0)) {
            for(String item : set0) {
                set.add(converter.deserialize(item, valueType));
            }
        }
        return set;
    }

    public CacheObject<K, Set<V>> membersCacheObject(K key) {
        validateKey(key);
        String key0 = wrapKey(key);
        DataType dataType = operations.execute(TYPE, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return stringRedisTemplate.type(key0);
                });
        if(DataType.STRING.equals(dataType)) {
            return new CacheObject<K, Set<V>>(key).asEmpty();
        }
        Set<String> set0 = operations.execute(SMEMBERS, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return setOps.members(key0);
                });
        Set<V> set = new HashSet<>();
        if(!CollectionUtils.isEmpty(set0)) {
            for(String item : set0) {
                set.add(converter.deserialize(item, valueType));
            }
        }
        return new CacheObject<K, Set<V>>(key, set);
    }

    public Long remove(K key, V... values) {
        if(values == null || values.length == 0) {
            return 0L;
        }
        validateKey(key);
        String key0 = wrapKey(key);
        String[] valueArray = toStringArray(values);
        return operations.execute(SREM, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return setOps.remove(key0, valueArray);
                }, valueArray);
    }

    public Set<V> scan(K key, String pattern, int scanSize) {
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(SSCAN, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    Set<V> values = new HashSet<>();
                    Cursor<String> cursor = setOps.scan(key0,
                            new ScanOptions.ScanOptionsBuilder()
                                    .match(pattern.concat("*"))
                                    .count(scanSize)
                                    .build());
                    String value;
                    while (cursor.hasNext()) {
                        value = cursor.next();
                        if(StringUtils.hasText(value)) {
                            values.add(converter.deserialize(value, valueType));
                        }
                    }
                    return values;
                }, pattern, scanSize);
    }

    private String[] toStringArray(V... values) {
        int length = values.length;
        String[] stringValues = new String[length];
        for(int i = 0; i < length; i++) {
            stringValues[i] = converter.serialize(values[i]);
        }
        return stringValues;
    }

}
