package com.mglj.totorial.framework.tool.data.redis;

import com.mglj.totorial.framework.tool.converter.StringConverter;
import com.mglj.totorial.framework.tool.data.redis.hash.BucketLocator;
import com.mglj.totorial.framework.tool.data.redis.operation.Operations;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.mglj.totorial.framework.tool.data.redis.RedisOperationEnum.*;


/**
 * 访问redis的模板工具，用key的hash值取模，决定键值放置到哪个hash桶里。
 *
 * Created by zsp on 2018/8/20.
 */
public class HashBucketOperationTemplate<K, V> extends AbstractOperationTemplate<K, V> {

    private final BucketLocator<K> bucketLocator;
    private final HashOperations<String, String, String> hashOps;
    private final RedisSerializer<String> hashKeySerializer;
    private final RedisSerializer<String> hashValueSerializer;

    public HashBucketOperationTemplate(Class<K> keyType,
                                       Class<V> valueType,
                                       StringRedisTemplate stringRedisTemplate,
                                       String namespace,
                                       String domain,
                                       Operations operations,
                                       BucketLocator<K> bucketLocator) {
        super(keyType, valueType, stringRedisTemplate, namespace, domain, operations);
        this.bucketLocator = bucketLocator;
        this.hashOps = stringRedisTemplate.opsForHash();
        this.hashKeySerializer = (RedisSerializer<String>)stringRedisTemplate.getHashKeySerializer();
        this.hashValueSerializer = (RedisSerializer<String>)stringRedisTemplate.getHashValueSerializer();
    }

    public HashBucketOperationTemplate(Class<K> keyType,
                                       Class<V> valueType,
                                       StringRedisTemplate stringRedisTemplate,
                                       String namespace,
                                       String domain,
                                       Operations operations,
                                       BucketLocator<K> bucketLocator,
                                       StringConverter<V> converter) {
        super(keyType, valueType, stringRedisTemplate, namespace, domain, operations, converter);
        this.bucketLocator = bucketLocator;
        this.hashOps = stringRedisTemplate.opsForHash();
        this.hashKeySerializer = (RedisSerializer<String>)stringRedisTemplate.getHashKeySerializer();
        this.hashValueSerializer = (RedisSerializer<String>)stringRedisTemplate.getHashValueSerializer();
    }

    public void set(K key, V value) {
        validateKey(key);
        String key0 = wrapKey(bucketLocator.getBucket(key));
        String field = bucketLocator.getIndex(key);
        String value0 = converter.serialize(value);
        operations.execute(HSET, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    hashOps.put(key0, field, value0);
                }, field, value0);
    }

    public void multiSet(Map<K, V> map) {
        Map<String, Map<String, String>> bucketMaps = new HashMap<>();
        for(Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            validateKey(key);
            String key0 = wrapKey(bucketLocator.getBucket(key));
            String field = bucketLocator.getIndex(key);
            String value0 = converter.serialize(entry.getValue());
            Map<String, String> bucketMap = bucketMaps.get(key0);
            if(bucketMap == null) {
                bucketMap = new HashMap<>();
                bucketMaps.put(key0, bucketMap);
            }
            bucketMap.put(field, value0);
        }
        operations.execute(HMSET, getKeyPrefix(), bucketMaps.keySet(),
                ()->{
                    return stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
                        @Nullable
                        @Override
                        public Object doInRedis(RedisConnection connection) throws DataAccessException {
                            Map<byte[], byte[]> byteMap;
                            for(Map.Entry<String, Map<String, String>> entry : bucketMaps.entrySet()) {
                                byteMap = new HashMap<>();
                                for(Map.Entry<String, String> entry2 : entry.getValue().entrySet()) {
                                    byteMap.put(keySerializer.serialize(entry2.getKey()),
                                            valueSerializer.serialize(entry2.getValue()));
                                }
                                connection.hMSet(keySerializer.serialize(entry.getKey()), byteMap);
                            }
                            return null;
                        }
                    });
                });
    }

    public V get(K key) {
        validateKey(key);
        String key0 = wrapKey(bucketLocator.getBucket(key));
        String field = bucketLocator.getIndex(key);
        return operations.execute(HGET, getKeyPrefix(), Arrays.asList(key0),
                () -> {
                    return converter.deserialize(hashOps.get(key0, field), valueType);
                }, field);
    }

    public List<V> multiGet(Collection<K> keys) {
        Map<String, Set<String>> bucketMaps = new HashMap<>();
        for(K key : keys) {
            validateKey(key);
            String key0 = wrapKey(bucketLocator.getBucket(key));
            String field = bucketLocator.getIndex(key);
            Set<String> fieldSet = bucketMaps.get(key0);
            if(fieldSet == null) {
                fieldSet = new HashSet<>();
                bucketMaps.put(key0, fieldSet);
            }
            fieldSet.add(field);
        }
        List<V> valueList = new ArrayList<>();
        List<String> stringList = operations.execute(HMGET, getKeyPrefix(), bucketMaps.keySet(),
                ()->{
                    List<Object> tempList = stringRedisTemplate.executePipelined(new RedisCallback<List<String>>() {
                        @Nullable
                        @Override
                        public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
                            List<String> list = new ArrayList<>();
                            List<byte[]> byteList;
                            for(Map.Entry<String, Set<String>> entry : bucketMaps.entrySet()) {
                                byteList = new ArrayList<>();
                                for(String filed : entry.getValue()) {
                                    byteList.add(hashKeySerializer.serialize(filed));
                                }
                                byteList = connection.hMGet(keySerializer.serialize(entry.getKey()),
                                        byteList.toArray(new byte[0][0]));
                                if(!CollectionUtils.isEmpty(byteList)) {
                                    for(byte[] byteValue : byteList) {
                                        list.add(hashValueSerializer.deserialize(byteValue));
                                    }
                                }
                            }
                            return list;
                        }
                    });
                    List<String> tempStringList = new ArrayList<>();
                    for(Object item : tempList) {
                        tempStringList.add((String)item);
                    }
                    return tempStringList;
                });
        if(!CollectionUtils.isEmpty(stringList)) {
            for(String stringValue : stringList) {
                valueList.add(converter.deserialize(stringValue, valueType));
            }
        }
        return valueList;
    }

}
