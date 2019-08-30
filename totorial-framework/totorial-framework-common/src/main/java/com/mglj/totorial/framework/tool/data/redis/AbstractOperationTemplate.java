package com.mglj.totorial.framework.tool.data.redis;

import com.mglj.totorial.framework.common.lang.CacheObject;
import com.mglj.totorial.framework.common.lang.LongHolder;
import com.mglj.totorial.framework.common.util.CollectionUtilsEx;
import com.mglj.totorial.framework.tool.converter.JsonConverter;
import com.mglj.totorial.framework.tool.converter.StringConverter;
import com.mglj.totorial.framework.tool.data.redis.operation.Operations;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.mglj.totorial.framework.tool.data.redis.RedisOperationEnum.*;


/**
 * 访问redis的抽象模板工具
 *
 * Created by zsp on 2018/8/9.
 */
public abstract class AbstractOperationTemplate<K, V> implements OperationTemplate{

    /**
     * 键的类型
     */
    protected final Class<K> keyType;
    /**
     * 值的类型
     */
    protected final Class<V> valueType;
    /**
     * redis访问工具
     */
    protected final StringRedisTemplate stringRedisTemplate;
    /**
     * 应用空间
     */
    protected String namespace;
    /**
     * 数据的领域名称，例如订单、商品等
     */
    protected String domain;
    /**
     * 批量操作的数据记录条数
     */
    protected int multiOperationBatchSize = 100;
    /**
     * 开启键过期时间的上下浮动特性
     */
    protected boolean enableKeyExpiredVolatility = true;
    /**
     * 对访问操作的包装，例如对于redis的访问操作增加计数、监控等行为
     */
    protected final Operations operations;
    /**
     * 数据的转换工具，例如json转换
     */
    protected final StringConverter<V> converter;

    protected final RedisSerializer<String> keySerializer;
    protected final RedisSerializer<String> valueSerializer;

    private final SecureRandom random = new SecureRandom();

    /**
     * 构建一个redis访问工具对象
     *
     * @param keyType               键的类型
     * @param valueType             值的类型
     * @param stringRedisTemplate   redis访问模板
     * @param namespace             数据的命名空间
     * @param domain                数据的领域
     * @param operations            数据访问操作
     */
    protected AbstractOperationTemplate(Class<K> keyType,
                                        Class<V> valueType,
                                        StringRedisTemplate stringRedisTemplate,
                                        String namespace,
                                        String domain,
                                        Operations operations) {
        this(keyType, valueType, stringRedisTemplate, namespace, domain,
                operations, new JsonConverter<>());
    }

    /**
     * 构建一个redis访问工具对象
     *
     * @param keyType               键的类型
     * @param valueType             值的类型
     * @param stringRedisTemplate   redis访问模板
     * @param namespace             数据的命名空间
     * @param domain                数据的领域
     * @param operations            数据访问操作
     * @param converter             数据的转换工具
     */
    protected AbstractOperationTemplate(Class<K> keyType,
                                        Class<V> valueType,
                                        StringRedisTemplate stringRedisTemplate,
                                        String namespace,
                                        String domain,
                                        Operations operations,
                                        StringConverter<V> converter) {
        Objects.requireNonNull(keyType, "The keyType is null.");
        KeyUtils.checkType(keyType);
        Objects.requireNonNull(valueType, "The valueType is null.");
        Objects.requireNonNull(stringRedisTemplate, "The stringRedisTemplate is null.");
        if(!StringUtils.hasText(namespace)){
            throw new NullPointerException("The namespace is null or empty.");
        }
        if(!StringUtils.hasText(domain)){
            throw new NullPointerException("The domain is null or empty.");
        }
        Objects.requireNonNull(operations, "The operations is null.");
        Objects.requireNonNull(converter, "The converter is null.");
        this.keyType = keyType;
        this.valueType = valueType;
        this.stringRedisTemplate = stringRedisTemplate;
        this.keySerializer = stringRedisTemplate.getStringSerializer();
        this.valueSerializer = (RedisSerializer<String>)stringRedisTemplate.getValueSerializer();
        this.namespace = namespace;
        this.domain = domain;
        this.operations = operations;
        this.converter = converter;
    }

    @Override
    public String getNamespace() {
        return this.namespace;
    }

    @Override
    public void setNamespace(String value) {
        this.namespace = value;
    }

    @Override
    public String getDomain() {
        return this.domain;
    }

    @Override
    public void setDomain(String value) {
        this.domain = value;
    }

    @Override
    public int getMultiOperationBatchSize() {
        return this.multiOperationBatchSize;
    }

    @Override
    public void setMultiOperationBatchSize(int value) {
        this.multiOperationBatchSize = value;
    }

    @Override
    public boolean isEnableKeyExpiredVolatility() {
        return enableKeyExpiredVolatility;
    }

    @Override
    public void setEnableKeyExpiredVolatility(boolean value) {
        this.enableKeyExpiredVolatility = value;
    }

    public StringConverter<V> getConverter() {
        return converter;
    }

    /**
     * 删除
     *
     * @param key
     */
    public Boolean delete(K key) {
        validateKey(key);
        return deleteString(wrapKey(key));
    }

    /**
     * 删除
     *
     * @param key
     * @return
     */
    protected final Boolean deleteString(String key) {
        return operations.execute(DEL, getKeyPrefix(), Arrays.asList(key),
                () -> {
                    return stringRedisTemplate.delete(key);
                });
    }

    /**
     * 删除
     *
     * @param keys
     */
    public Long delete(Collection<K> keys) {
        validateKey(keys);
        return deleteString(wrapKey(keys));
    }

    /**
     * 删除
     *
     * @param keys
     * @return
     */
    protected final Long deleteString(Collection<String> keys) {
        int batchSize = multiOperationBatchSize;
        if(keys.size() > batchSize) {
            LongHolder count = new LongHolder(0L);
            CollectionUtilsEx.foreachSubList(new ArrayList<>(keys), batchSize,
                    (loop, startIndex, endIndex, subList) -> {
                        long delta = deleteString0(subList);
                        count.increment(delta);
                    });
            return count.get();
        }
        return deleteString0(keys);
    }

    private Long deleteString0(Collection<String> keys) {
        return operations.execute(DEL, getKeyPrefix(), keys,
                () -> {
                    return stringRedisTemplate.delete(keys);
                });
    }

    /**
     * 判断键是否存在
     *
     * @param key
     * @return
     */
    public Boolean hasKey(K key) {
        validateKey(key);
        return hasStringKey(wrapKey(key));
    }

    /**
     * 判断键是否存在
     *
     * @param key
     * @return
     */
    protected final Boolean hasStringKey(String key) {
        return operations.execute(EXISTS, getKeyPrefix(), Arrays.asList(key),
                () -> {
                    return stringRedisTemplate.hasKey(key);
                });
    }

    /**
     * 给键设置过期时间
     *
     * @param key
     * @param seconds
     * @return
     */
    public Boolean expire(K key, long seconds) {
        if(seconds < 1) {
            return false;
        }
        validateKey(key);
        return expireString(wrapKey(key), seconds);
    }

    /**
     * 给键设置过期时间
     *
     * @param key
     * @param seconds
     * @return
     */
    protected final Boolean expireString(String key, long seconds) {
        return operations.execute(EXPIRE, getKeyPrefix(), Arrays.asList(key),
                () -> {
                    return stringRedisTemplate.expire(key,
                            seconds + getExpiredSecondsVolatility(seconds),
                            TimeUnit.SECONDS);
                }, seconds);
    }

    /**
     * 给键设置过期时间
     *
     * @param keys
     * @param seconds
     */
    public void expire(Collection<K> keys, long seconds) {
        if(seconds < 1) {
            return;
        }
        validateKey(keys);
        expireString(wrapKey(keys), seconds);
    }

    /**
     * 给键设置过期时间
     *
     * @param keys
     * @param seconds
     */
    protected final void expireString(Collection<String> keys, long seconds) {
        int batchSize = multiOperationBatchSize;
        if(keys.size() > batchSize) {
            CollectionUtilsEx.foreachSubList(new ArrayList<>(keys), batchSize,
                    (loop, startIndex, endIndex, subList) -> {
                        expireString0(subList, seconds);
                    });
        } else {
            expireString0(keys, seconds);
        }
    }

    private void expireString0(Collection<String> keys, long seconds) {
        operations.execute(EXPIRE, getKeyPrefix(), keys,
                () -> {
                    return stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
                        @Nullable
                        @Override
                        public Object doInRedis(RedisConnection connection) throws DataAccessException {
                            for (String key : keys) {
                                connection.expire(keySerializer.serialize(key),
                                        seconds + getExpiredSecondsVolatility(seconds));
                            }
                            return null;
                        }
                    });
                }, seconds);
    }

    /**
     * 给键设置过期时间
     *
     * @param key
     * @param date
     */
    public Boolean expireAt(K key, Date date) {
        if(date == null || date.getTime() <= System.currentTimeMillis()) {
            return false;
        }
        validateKey(key);
        return expireStringAt(wrapKey(key), date);
    }

    /**
     * 给键设置过期时间
     *
     * @param key
     * @param date
     * @return
     */
    protected final Boolean expireStringAt(String key, Date date) {
        return operations.execute(EXPIREAT, getKeyPrefix(), Arrays.asList(key),
                () -> {
                    return stringRedisTemplate.expireAt(key, date);
                }, date);
    }

    /**
     * 给键设置过期时间
     *
     * @param keys
     * @param date
     */
    public void expireAt(Collection<K> keys, Date date) {
        if(CollectionUtils.isEmpty(keys) || date == null || date.getTime() <= System.currentTimeMillis()) {
            return;
        }
        validateKey(keys);
        expireStringAt(wrapKey(keys), date);
    }

    /**
     * 给键设置过期时间
     * @param keys
     * @param date
     */
    protected final void expireStringAt(Collection<String> keys, Date date) {
        int batchSize = multiOperationBatchSize;
        if(keys.size() > batchSize) {
            CollectionUtilsEx.foreachSubList(new ArrayList<>(keys), batchSize,
                    (loop, startIndex, endIndex, subList) -> {
                        expireStringAt0(subList, date);
                    });
        } else {
            expireStringAt0(keys, date);
        }
    }

    private void expireStringAt0(Collection<String> keys, Date date) {
        operations.execute(EXPIRE, getKeyPrefix(), keys,
                ()->{
                    return stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
                        @Nullable
                        @Override
                        public Object doInRedis(RedisConnection connection) throws DataAccessException {
                            try {
                                for(String key : keys) {
                                    connection.pExpireAt(keySerializer.serialize(key), date.getTime());
                                }
                            } catch (Exception e) {
                                for (String key : keys) {
                                    connection.expireAt(keySerializer.serialize(key), date.getTime() / 1000);
                                }
                            }
                            return null;
                        }
                    });
                }, date);
    }

    /**
     * 获取键剩余过期的时间（秒）
     *
     * @param key
     * @return
     */
    public Long getExpire(K key) {
        validateKey(key);
        return getExpireString(wrapKey(key));
    }

    protected final Long getExpireString(String key) {
        return operations.execute(TTL, getKeyPrefix(), Arrays.asList(key),
                () -> {
                    return stringRedisTemplate.getExpire(key);
                });
    }

    /**
     * 获取扫描范围内的所有键
     *
     * @param pattern   匹配的字符，左匹配
     * @param scanSize  扫描的最大记录条数
     * @return
     */
    public Set<K> getKeys(K pattern, int scanSize) {
        if(scanSize < 1) {
            throw new IllegalArgumentException("The scanSize should be than 0.");
        }
        String key0 = pattern == null ? wrapKey(""): wrapKey(pattern);
        String keyPrefix = getKeyPrefix();
        return operations.execute(SCAN, keyPrefix, Arrays.asList(key0),
                ()->{
                    return stringRedisTemplate.execute(new RedisCallback<Set<K>>() {
                        @Override
                        public Set<K> doInRedis(RedisConnection connection) throws DataAccessException {
                            Set<K> keys = new HashSet<>();
                            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder()
                                    .match(key0.concat("*")).count(scanSize).build());
                            String key;
                            while (cursor.hasNext()) {
                                key = new String(cursor.next());
                                if(StringUtils.hasText(key)) {
                                    keys.add(unwrapKey(key));
                                }
                            }
                            return keys;
                        }
                    });
                }, scanSize);
    }

    protected final CacheObject<K, V> asCacheObject(K key, String value) {
        if(value == null) {
            return new CacheObject<K, V>(key);
        } else if(CacheObject.isEmptyValueForBreakdownPrevent(value)) {
            return new CacheObject<K, V>(key).asEmpty();
        } else {
            if(converter != null) {
                return new CacheObject<K, V>(key, converter.deserialize(value, valueType));
            } else {
                return new CacheObject<K, V>(key, (V)value);
            }
        }
    }

    /**
     * 检验键的合法性
     *
     * @param key
     */
    protected void validateKey(K key) {
        Objects.requireNonNull(key, "The key is null.");
        KeyUtils.checkType(key.getClass());
    }

    /**
     * 检验键的合法性
     *
     * @param keys
     */
    protected void validateKey(Collection<K> keys) {
        if(CollectionUtils.isEmpty(keys)) {
            throw new NullPointerException("The keys is null or empty.");
        }
        for(K key : keys) {
            validateKey(key);
        }
    }

    /**
     * 包装键值，把key包装为"namespace:domain:key"的格式
     *
     * @param key
     * @return
     */
    protected final <TKey> String wrapKey(TKey key) {
        return KeyUtils.wrapKey(namespace, domain, key);
    }

    /**
     * 包装键值，把key包装为"namespace:domain:key"的格式
     *
     * @param keys
     * @return
     */
    protected final List<String> wrapKey(Collection<K> keys) {
        List<String> keyList = new ArrayList<>();
        for(K key : keys) {
            keyList.add(wrapKey(key));
        }
        return keyList;
    }

    /**
     * 拆装键值，把"namespace:domain:key"的格式还原为key
     *
     * @param stringKey
     * @return
     */
    protected final K unwrapKey(String stringKey) {
        return KeyUtils.unwrapKey(stringKey, keyType);
    }

    /**
     * 获取键的前缀，一般是"namespace:domain:"的格式
     * @return
     */
    protected final String getKeyPrefix() {
        return KeyUtils.getKeyPrefix(namespace, domain);
    }

    /**
     * 转换成键值为String的Map
     *
     * @param map
     * @return
     */
    protected final Map<String, String> toStringMap(Map<K, V> map) {
        Map<String, String> map0 = new HashMap<>();
        for(Map.Entry<K, V> entry : map.entrySet()) {
            map0.put(wrapKey(entry.getKey()), converter.serialize(entry.getValue()));
        }
        return map0;
    }

    /**
     * 获取过期时间的上下浮动数
     *
     * @param seconds
     * @return
     */
    protected long getExpiredSecondsVolatility(long seconds) {
        long value;
        if(!enableKeyExpiredVolatility || seconds < 3) {
            value = 0;
        } else if(seconds < 6) {
            value = random.nextInt(2);
        } else if(seconds < 61) {
            value = random.nextInt(5);
        } else if(seconds < 601) {
            value = random.nextInt(10);
        } else {
            value = random.nextInt(30);
        }
        if(value > 0 && random.nextInt() < 0) {
            value = - value;
        }

        return value;
    }

    private K convertKey(String stringKey) {
        return KeyUtils.convertKey(stringKey, keyType);
    }

}
