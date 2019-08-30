package com.mglj.totorial.framework.tool.data.redis.tool;

import com.mglj.totorial.framework.tool.data.redis.KeyUtils;
import com.mglj.totorial.framework.tool.data.redis.operation.Operations;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.mglj.totorial.framework.tool.data.redis.RedisConstants.REDIS_KEY_WORD_HYPHEN;
import static com.mglj.totorial.framework.tool.data.redis.RedisOperationEnum.*;


/**
 * Created by zsp on 2018/9/20.
 */
public class DirectAccessTemplate {

    private final StringRedisTemplate stringRedisTemplate;
    private final Operations operations;
    private final ValueOperations<String, String> valueOps;
    private final HashOperations<String, String, String> hashOps;

    public DirectAccessTemplate(StringRedisTemplate stringRedisTemplate,
                                Operations operations) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.operations = operations;
        this.valueOps = stringRedisTemplate.opsForValue();
        this.hashOps = stringRedisTemplate.opsForHash();
    }

    public Boolean delete(DirectAccessRequest request) {
        String keyPrefix = getKeyPrefix(request);
        String key = buildKey(request);
        return operations.execute(DEL, keyPrefix, Arrays.asList(key),
                () -> {
                    return stringRedisTemplate.delete(key);
                });
    }

    public Long getExpiredSeconds(DirectAccessRequest request) {
        String keyPrefix = getKeyPrefix(request);
        String key = buildKey(request);
        return operations.execute(TTL, keyPrefix, Arrays.asList(key),
                () -> {
                    return stringRedisTemplate.getExpire(key);
                });
    }

    public Set<String> getKeys(DirectAccessRequest request) {
        int scanSize = request.getScanSize() == null ? 100 : request.getScanSize();
        String keyPrefix = getKeyPrefix(request);
        String key = buildKey(request);
        return operations.execute(SCAN, keyPrefix, Arrays.asList(key),
                ()->{
                    return stringRedisTemplate.execute(new RedisCallback<Set<String>>() {
                        @Override
                        public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
                            Set<String> keys = new HashSet<>();
                            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder()
                                    .match(key.concat("*")).count(scanSize).build());
                            String key;
                            while (cursor.hasNext()) {
                                key = new String(cursor.next());
                                if(StringUtils.hasText(key)) {
                                    keys.add(KeyUtils.unwrapKey(key, String.class));
                                }
                            }
                            return keys;
                        }
                    });
                }, scanSize);
    }

    public void setString(DirectAccessRequest request) {
        String keyPrefix = getKeyPrefix(request);
        String key = buildKey(request);
        operations.execute(SET, keyPrefix, Arrays.asList(key),
                () -> {
                    Long expiredSeconds = request.getExpiredSeconds();
                    if(expiredSeconds == null || expiredSeconds < 1) {
                        valueOps.set(key, request.getValue());
                    } else {
                        valueOps.set(key, request.getValue(),
                                expiredSeconds, TimeUnit.SECONDS);
                    }
                });
    }

    public DirectAccessResponse<String> getString(DirectAccessRequest request) {
        String keyPrefix = getKeyPrefix(request);
        String key = buildKey(request);
        String value = operations.execute(GET, keyPrefix, Arrays.asList(key),
                () -> {
                    return valueOps.get(key);
                });
        DirectAccessResponse<String> response = new DirectAccessResponse<>(value);
        buildResponseWithExpiredSeconds(request, response, keyPrefix, key);
        return response;
    }

    /**
     * 获取针对StringListOperationTemplate存储的值
     *
     * @param request
     * @return
     */
    public DirectAccessResponse<List<Bucket<String>>> getStringBucket(DirectAccessRequest request) {
        String keyPrefix = getKeyPrefix(request);
        String key = buildKey(request);
        List<Bucket<String>> list = new ArrayList<>();
        String value, lastKey = null;
        for(int index = 0, len = request.getBucketSize(); len > 0 && index < len; index++) {
            String key0 = key  + REDIS_KEY_WORD_HYPHEN +  index;
            value = operations.execute(GET, keyPrefix, Arrays.asList(key0),
                    () -> {
                        return valueOps.get(key0);
                    });
            if(value == null) {
                break;
            }
            lastKey = key0;
            list.add(new Bucket<>(index, value));
        }
        DirectAccessResponse<List<Bucket<String>>> response = new DirectAccessResponse<>(list);
        buildResponseWithExpiredSeconds(request, response, keyPrefix, lastKey);
        return response;
    }

    /**
     * 获取针对HashBucketOperationTemplate存储的值
     *
     * @param request
     * @return
     */
    public DirectAccessResponse<List<Bucket<Map<String, String>>>> getHashBucket(DirectAccessRequest request) {
        String keyPrefix = getKeyPrefix(request);
        List<Bucket<Map<String, String>>> list = new ArrayList<>();
        Map<String, String> entries;
        String lastKey = null;
        for(int index = 0, len = request.getBucketSize(); index < len; index++) {
            String key0 = keyPrefix + index;
            entries = operations.execute(HGETALL, keyPrefix, Arrays.asList(key0),
                    () -> {
                        return hashOps.entries(key0);
                    });
            if(CollectionUtils.isEmpty(entries)) {
                continue;
            }
            lastKey = key0;
            list.add(new Bucket(index, entries));
        }
        DirectAccessResponse<List<Bucket<Map<String, String>>>> response = new DirectAccessResponse<>(list);
        buildResponseWithExpiredSeconds(request, response, keyPrefix, lastKey);
        return response;
    }

    private void buildResponseWithExpiredSeconds(DirectAccessRequest request, DirectAccessResponse response,
                                                 String keyPrefix, String key) {
        Long expiredSeconds = operations.execute(TTL, keyPrefix, Arrays.asList(key),
                () -> {
                    return stringRedisTemplate.getExpire(key);
                });
        response.setExpiredSeconds(expiredSeconds);
    }

    private String buildKey(DirectAccessRequest request) {
        return KeyUtils.wrapKey(request.getNamespace(), request.getDomain(), request.getKey());
    }

    private String getKeyPrefix(DirectAccessRequest request) {
        return KeyUtils.getKeyPrefix(request.getNamespace(), request.getDomain());
    }

}
