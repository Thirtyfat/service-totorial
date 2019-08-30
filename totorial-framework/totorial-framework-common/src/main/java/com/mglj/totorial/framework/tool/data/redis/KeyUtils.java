package com.mglj.totorial.framework.tool.data.redis;

import java.util.Objects;

import static com.mglj.totorial.framework.tool.data.redis.RedisConstants.REDIS_KEY_HYPHEN;


/**
 * Created by zsp on 2018/9/20.
 */
public class KeyUtils {

    /**
     * 包装键值，把key包装为"namespace:domain:key"的格式
     *
     * @param namespace
     * @param domain
     * @param key
     * @param <TKey>
     * @return
     */
    public static <TKey> String wrapKey(String namespace, String domain, TKey key) {
        return getKeyPrefix(namespace, domain)
                .concat(key == null ? "" : String.valueOf(key));
    }

    /**
     * 拆装键值，把"namespace:domain:key"的格式还原为key
     *
     * @param stringKey
     * @return
     */
    public static <TKey> TKey unwrapKey(String stringKey, Class<TKey> keyType) {
        return convertKey(stringKey.substring(stringKey.lastIndexOf(REDIS_KEY_HYPHEN) + 1), keyType);
    }

    /**
     * 获取键的前缀，一般是"namespace:domain:"的格式
     *
     * @param namespace
     * @param domain
     * @return
     */
    public static String getKeyPrefix(String namespace, String domain) {
        namespace = namespace == null ? "": namespace;
        domain = domain == null ? "" : domain;
        return namespace
                .concat(REDIS_KEY_HYPHEN)
                .concat(domain)
                .concat(REDIS_KEY_HYPHEN);
    }

    /**
     * 检查key的类型的合法性
     *
     * @param type
     */
    public static void checkType(Class<?> type) {
        Objects.requireNonNull(type, "The type is null.");
        if(Objects.equals(String.class, type)) {
            return;
        } else if(Objects.equals(byte.class, type) || Objects.equals(Byte.class, type)) {
            return;
        } else if(Objects.equals(short.class, type) || Objects.equals(Short.class, type)) {
            return;
        } else if(Objects.equals(int.class, type) || Objects.equals(Integer.class, type)) {
            return;
        } else if(Objects.equals(long.class, type) || Objects.equals(Long.class, type)) {
            return;
        } else if(Objects.equals(float.class, type) || Objects.equals(Float.class, type)) {
            return;
        } else if(Objects.equals(double.class, type) || Objects.equals(Double.class, type)) {
            return;
        } else if(Objects.equals(boolean.class, type) || Objects.equals(Boolean.class, type)) {
            return;
        }
        throw new IllegalArgumentException("不支持的键类型");
    }

    /**
     * 根据提供的类型转换key
     *
     * @param stringKey
     * @param keyType
     * @param <TKey>
     * @return
     */
    public static <TKey> TKey convertKey(String stringKey, Class<TKey> keyType) {
        Objects.requireNonNull(keyType, "The keyType is null.");
        if(stringKey == null) {
            return null;
        }
        if(Objects.equals(String.class, keyType)) {
            return (TKey)stringKey;
        } else if(Objects.equals(byte.class, keyType) || Objects.equals(Byte.class, keyType)) {
            return (TKey)Byte.valueOf(stringKey);
        } else if(Objects.equals(short.class, keyType) || Objects.equals(Short.class, keyType)) {
            return (TKey)Short.valueOf(stringKey);
        } else if(Objects.equals(int.class, keyType) || Objects.equals(Integer.class, keyType)) {
            return (TKey)Integer.valueOf(stringKey);
        } else if(Objects.equals(long.class, keyType) || Objects.equals(Long.class, keyType)) {
            return (TKey)Long.valueOf(stringKey);
        } else if(Objects.equals(float.class, keyType) || Objects.equals(Float.class, keyType)) {
            return (TKey)Float.valueOf(stringKey);
        } else if(Objects.equals(double.class, keyType) || Objects.equals(Double.class, keyType)) {
            return (TKey)Double.valueOf(stringKey);
        } else if(Objects.equals(boolean.class, keyType) || Objects.equals(Boolean.class, keyType)) {
            return (TKey)Boolean.valueOf(stringKey);
        }
        throw new IllegalArgumentException("不支持的键类型");
    }

}
