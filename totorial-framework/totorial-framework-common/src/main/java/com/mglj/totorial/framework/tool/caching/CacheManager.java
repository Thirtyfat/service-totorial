package com.mglj.totorial.framework.tool.caching;

import com.mglj.totorial.framework.tool.data.redis.RedisConstants;
import com.mglj.totorial.framework.tool.data.redis.StringOperationTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by zsp on 2018/8/10.
 */
public final class CacheManager {

    public final static String DOMAIN_CACHE_MANAGER = "cache-manager";

    private final AbstractCacheFactory cacheFactory;
    private final StringOperationTemplate<String, Long> stringOperationTemplate;

    private boolean enableStatHitRate;

    public CacheManager(CacheFactory cacheFactory, StringOperationTemplate<String, Long> stringOperationTemplate) {
        Objects.requireNonNull(cacheFactory, "The cacheFactory is null.");
        Objects.requireNonNull(stringOperationTemplate, "The stringOperationTemplate is null.");
        this.cacheFactory = (AbstractCacheFactory)cacheFactory;
        this.stringOperationTemplate = stringOperationTemplate;
    }

    <K, V> void incrHit(Cache<K, V> cache, K key) {
        if(enableStatHitRate) {
            cacheFactory.asyncExecute(
                    () -> {
                        stringOperationTemplate.increment(buildHitKey(cache), 1);
                    });
        }
    }

    <K, V> void incrHit(Cache<K, V> cache, Collection<K> keys) {
        if(enableStatHitRate && !CollectionUtils.isEmpty(keys)) {
            cacheFactory.asyncExecute(
                    () -> {
                        stringOperationTemplate.increment(buildHitKey(cache), keys.size());
                    });
        }
    }

    <K, V> void incrEmptyHit(Cache<K, V> cache, K key) {
        if(enableStatHitRate) {
            cacheFactory.asyncExecute(
                    () -> {
                        stringOperationTemplate.increment(buildEmptyHitKey(cache), 1);
                    });
        }
    }

    <K, V> void incrEmptyHit(Cache<K, V> cache, Collection<K> keys) {
        if(enableStatHitRate && !CollectionUtils.isEmpty(keys)) {
            cacheFactory.asyncExecute(
                    () -> {
                        stringOperationTemplate.increment(buildEmptyHitKey(cache), keys.size());
                    });
        }
    }

    <K, V> void incrMiss(Cache<K, V> cache, K key) {
        if(enableStatHitRate) {
            cacheFactory.asyncExecute(
                    () -> {
                        stringOperationTemplate.increment(buildMissKey(cache), 1);
                    });
        }
    }

    <K, V> void incrMiss(Cache<K, V> cache, Collection<K> keys) {
        if(enableStatHitRate && !CollectionUtils.isEmpty(keys)) {
            cacheFactory.asyncExecute(
                    () -> {
                        stringOperationTemplate.increment(buildMissKey(cache), keys.size());
                    });
        }
    }

    <K, V> void incr(Cache<K, V> cache,
                         Collection<K> hitKeys,
                         Collection<K> emptyHitKeys,
                         Collection<K> missKeys) {
        if(enableStatHitRate) {
            if(!CollectionUtils.isEmpty(hitKeys)) {
                cacheFactory.asyncExecute(
                        () -> {
                            stringOperationTemplate.increment(buildHitKey(cache), hitKeys.size());
                        });
            }
            if(!CollectionUtils.isEmpty(emptyHitKeys)) {
                cacheFactory.asyncExecute(
                        () -> {
                            stringOperationTemplate.increment(buildEmptyHitKey(cache), emptyHitKeys.size());
                        });
            }
            if(!CollectionUtils.isEmpty(missKeys)) {
                cacheFactory.asyncExecute(
                        () -> {
                            stringOperationTemplate.increment(buildMissKey(cache), missKeys.size());
                        });
            }
        }
    }

    public <K, V> long getHits(Cache<K, V> cache) {
        Long value = stringOperationTemplate.get(buildHitKey(cache));
        if(value != null) {
            return value;
        } else {
            return 0;
        }
    }

    public <K, V> long getEmptyHits(Cache<K, V> cache) {
        Long value = stringOperationTemplate.get(buildEmptyHitKey(cache));
        if(value != null) {
            return value;
        } else {
            return 0;
        }
    }

    public <K, V> long getMisses(Cache<K, V> cache) {
        Long value = stringOperationTemplate.get(buildMissKey(cache));
        if(value != null) {
            return value;
        } else {
            return 0;
        }
    }

    public List<Cache> getCacheList() {
        return cacheFactory.getCacheList();
    }

    /**
     * 获取缓存管理对象
     *
     * @param name	缓存管理对象名称
     * @return
     */
    public Cache getCache(String name) {
        if(StringUtils.hasText(name)) {
            List<Cache> _cacheList = cacheFactory.getCacheList();
            for(Cache cache : _cacheList) {
                if(name.equals(cache.getName())) {
                    return cache;
                }
            }
        }
        return null;
    }

    void shutdown() {

    }

    private final String buildHitKey(Cache cache) {
        return cache.getName().concat(RedisConstants.REDIS_KEY_HYPHEN).concat("hit");
    }

    private final String buildEmptyHitKey(Cache cache) {
        return cache.getName().concat(RedisConstants.REDIS_KEY_HYPHEN).concat("ehit");
    }

    private final String buildMissKey(Cache cache) {
        return cache.getName().concat(RedisConstants.REDIS_KEY_HYPHEN).concat("mis");
    }

    public boolean isEnableStatHitRate() {
        return enableStatHitRate;
    }

    public void setEnableStatHitRate(boolean enableStatHitRate) {
        this.enableStatHitRate = enableStatHitRate;
    }

}
