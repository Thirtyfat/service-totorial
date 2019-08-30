package com.mglj.totorial.framework.tool.data.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mglj.totorial.framework.tool.converter.StringConverter;
import com.mglj.totorial.framework.tool.data.redis.operation.Operations;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.mglj.totorial.framework.tool.data.redis.RedisOperationEnum.*;


/**
 * Created by zsp on 2018/8/9.
 */
public class ZSetOperationTemplate<K, V> extends AbstractOperationTemplate<K, V> {
    private final ZSetOperations<String, String> zsetOps;
    protected ObjectMapper objectMapper = new ObjectMapper();

    protected ZSetOperationTemplate(Class<K> keyType,
                                    Class<V> valueType,
                                    StringRedisTemplate stringRedisTemplate,
                                    String namespace,
                                    String domain,
                                    Operations operations) {
        super(keyType, valueType, stringRedisTemplate, namespace, domain, operations);
        this.zsetOps = stringRedisTemplate.opsForZSet();
    }

    protected ZSetOperationTemplate(Class<K> keyType,
                                    Class<V> valueType,
                                    StringRedisTemplate stringRedisTemplate,
                                    String namespace,
                                    String domain,
                                    Operations operations,
                                    StringConverter<V> converter) {
        super(keyType, valueType, stringRedisTemplate, namespace, domain, operations, converter);
        this.zsetOps = stringRedisTemplate.opsForZSet();
    }


    public Boolean add(K key, V value, double score) {
        validateKey(key);
        String key0 = wrapKey(key);
        String value0 = converter.serialize(value);
        return operations.execute(ZADD, getKeyPrefix(), Arrays.asList(key0), () -> {
            return zsetOps.add(key0, value0, score);
        }, value0, score);
    }



  /*  public  Long add(K key, Set<ZSetOperations.TypedTuple<V>> tuples){
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(ZADD, getKeyPrefix(), Arrays.asList(key0), () -> {
            return zsetOps.add(key0,tuples);
        }, tuples);

    }*/

    public Long remove(K key, V... values) {
        validateKey(key);
        String key0 = wrapKey(key);
        int length = values.length;
        String[] valueArray = new String[length];
        for (int i = 0; i < length; i++) {
            valueArray[i] = converter.serialize(values[i]);
        }
        return operations.execute(ZREM, getKeyPrefix(), Arrays.asList(key0), () -> {
            return zsetOps.remove(key0, valueArray);
        }, valueArray);

    }

    public Long rank(K key, Object o) {
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(ZRANK, getKeyPrefix(), Arrays.asList(key0), () -> {
            return zsetOps.rank(key0, o);
        } );
    }


    public Long reverseRank(K key, Object o) {
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(ZREVRANK, getKeyPrefix(), Arrays.asList(key0), () -> {
            return zsetOps.reverseRank(key0, o);
        } );
    }

    public Set<V> range(K key, long start, long end) {
        validateKey(key);
        String key0 = wrapKey(key);
        Set<String> set0 = operations.execute(ZRANGE, getKeyPrefix(), Arrays.asList(key0), () -> {
            return zsetOps.range(key0, start, end);
        }, start, end);
        Set<V> set = new HashSet<>();
        if (!CollectionUtils.isEmpty(set0)) {
            for (String item : set0) {
                set.add(converter.deserialize(item, valueType));
            }
        }
        return set;
    }

  /*  public Set<ZSetOperations.TypedTuple<V>> rangeWithScores(K key, long start, long end){
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(ZSCAN, getKeyPrefix(), Arrays.asList(key0), () -> {
            Set<ZSetOperations.TypedTuple<String>> cursor = zsetOps.rangeWithScores(key0,
                    start,end);
            return cursor;
        }, start, end);
    }*/

    public Set<V> rangeByScore(K key, double min, double max) {
        validateKey(key);
        String key0 = wrapKey(key);
        Set<String> set0 = operations.execute(ZRANGEBYSCORE, getKeyPrefix(), Arrays.asList(key0), () -> {
            return zsetOps.rangeByScore(key0, min, max);
        }, min, max);
        Set<V> set = new HashSet<>();
        if (!CollectionUtils.isEmpty(set0)) {
            for (String item : set0) {
                set.add(converter.deserialize(item, valueType));
            }
        }
        return set;

    }

    public Set<V> rangeByScore(K key, double min, double max, long offset, long count) {
        validateKey(key);
        String key0 = wrapKey(key);
        Set<String> set0 = operations.execute(ZRANGEBYSCORE, getKeyPrefix(), Arrays.asList(key0), () -> {
            return zsetOps.rangeByScore(key0, min, max, offset, count);
        }, min, max, offset, count);
        Set<V> set = new HashSet<>();
        if (!CollectionUtils.isEmpty(set0)) {
            for (String item : set0) {
                set.add(converter.deserialize(item, valueType));
            }
        }
        return set;

    }


    public Set<V> reverseRange(K key, long start, long end) {
        validateKey(key);
        String key0 = wrapKey(key);
        Set<String> set0 = operations.execute(ZREVRANGE, getKeyPrefix(), Arrays.asList(key0), () -> {
            return zsetOps.reverseRange(key0, start, end);
        }, start, end);
        Set<V> set = new HashSet<>();
        if (!CollectionUtils.isEmpty(set0)) {
            for (String item : set0) {
                set.add(converter.deserialize(item, valueType));
            }
        }
        return set;


    }

    public Set<V> reverseRangeByScore(K key, double min, double max) {
        validateKey(key);
        String key0 = wrapKey(key);
        Set<String> set0 = operations.execute(ZREVRANGE, getKeyPrefix(), Arrays.asList(key0), () -> {
            return zsetOps.reverseRangeByScore(key0, min, max);
        }, min, max);
        Set<V> set = new HashSet<>();
        if (!CollectionUtils.isEmpty(set0)) {
            for (String item : set0) {
                set.add(converter.deserialize(item, valueType));
            }
        }
        return set;


    }

    public Set<V> reverseRangeByScore(K key, double min, double max, long offset, long count) {
        validateKey(key);
        String key0 = wrapKey(key);
        Set<String> set0 = operations.execute(ZREVRANGE, getKeyPrefix(), Arrays.asList(key0), () -> {
            return zsetOps.reverseRangeByScore(key0, min, max, offset, count);
        }, min, max, offset, count);
        Set<V> set = new HashSet<>();
        if (!CollectionUtils.isEmpty(set0)) {
            for (String item : set0) {
                set.add(converter.deserialize(item, valueType));
            }
        }
        return set;


    }


    public Long count(K key, double min, double max){
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(ZCOUNT, getKeyPrefix(), Arrays.asList(key0), () -> {
            return zsetOps.count(key0, min,max);
        }, min,max);

    }
    public Long size(K key){
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(ZCARD, getKeyPrefix(), Arrays.asList(key0), () -> {
            return zsetOps.size(key0);
        });
    }
    public Long zCard(K key){
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(ZCARD, getKeyPrefix(), Arrays.asList(key0), () -> {
            return zsetOps.zCard(key0);
        });
    }
    public Double score(K key, Object o){
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(ZSCORE, getKeyPrefix(), Arrays.asList(key0), () -> {
            return zsetOps.score(key0, o);
        });

    }



    public Long removeRange(K key, long start, long end){
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(ZREMRANGEBYRANK, getKeyPrefix(), Arrays.asList(key0), () -> {
            return zsetOps.removeRange(key0,start,end);
        },start,end);

    }


    public Long removeRangeByScore(K key, double min, double max){
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(ZREMRANGEBYSCORE, getKeyPrefix(), Arrays.asList(key0), () -> {
            return zsetOps.removeRangeByScore(key0,min,max);
        },min,max);

    }




    public Set<V> scan(K key, String pattern, int scanSize) {
        validateKey(key);
        String key0 = wrapKey(key);
        return operations.execute(ZSCAN, getKeyPrefix(), Arrays.asList(key0), () -> {
            Set<V> values = new HashSet<>();
            Cursor<ZSetOperations.TypedTuple<String>> cursor = zsetOps.scan(key0,
                    new ScanOptions.ScanOptionsBuilder()
                            .match(pattern.concat("*"))
                            .count(scanSize)
                            .build());
            ZSetOperations.TypedTuple<String> value;
            while (cursor.hasNext()) {
                value = cursor.next();
                if (value != null) {
                    values.add(converter.deserialize(String.valueOf(value), valueType));
                }
            }
            return values;
        }, pattern, scanSize);
    }


}
