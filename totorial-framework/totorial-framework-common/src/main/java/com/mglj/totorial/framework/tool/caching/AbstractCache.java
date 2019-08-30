package com.mglj.totorial.framework.tool.caching;

import com.mglj.totorial.framework.common.lang.CacheObject;
import com.mglj.totorial.framework.tool.caching.filter.CachePreHandler;
import com.mglj.totorial.framework.tool.caching.filter.PreHandleResult;
import com.mglj.totorial.framework.tool.caching.filter.PreHandleResultEnum;
import com.mglj.totorial.framework.tool.caching.provider.CacheProvider;
import com.mglj.totorial.framework.tool.concurrent.locks.SimpleDisLock;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 实现了缓存的基本操作，具体的缓存存储访问由子类实现；
 *
 * 对getAndFetch方法的缓存写进行加锁，以保证多线程并发下数据的安全性；
 * 其它方法，如add,get,remove,clear等未提供对缓存读写方法的互斥，实现子类自己保证数据多线程并发下的安全性。
 *
 * 更新数据库数据时，建议直接淘汰缓存，而不是更新缓存，这样可避免在多线程并发下数据不安全的问题，例如：
 * 假如A线程在更新数据库后，直接更新缓存；B线程在A线程更新数据库前从数据库取到旧数据然后更新缓存，
 * 有可能覆盖A对缓存的更新结果；因此直接更新缓存是多余的操作。
 * 建议A线程在更新数据库时，做一个2次删除操作，顺序是：先删除缓存->更新数据库->延时删除缓存（根据经验设置延时时间），
 * 这样一定程度上避免B线程在A线程更新数据库前从数据库加载到旧数据去更新缓存，缓存存储的数据与数据库不一致。
 * 另外，对缓存设置时效，也可一定程度上保证缓存存储的数据与数据库的一致。
 *
 * Created by zsp on 2018/8/17.
 */
public abstract class AbstractCache<K, V> implements Cache<K, V> {

    protected final CacheProvider<K, V> cacheProvider;
    protected final AbstractCacheFactory cacheFactory;
    protected final CacheManager cacheManager;

    protected String name;
    protected String description;
    protected int expiredSeconds = 600;
    protected boolean enableBreakdownPrevent = true;
    protected int breakdownPreventExpiredSeconds = 5;
    protected boolean enableExtendExpiredSecondsWhenHit = true;

    public AbstractCache(CacheManager cacheManager,
                         CacheProvider<K, V> cacheProvider,
                         CacheFactory cacheFactory) {
        Objects.requireNonNull(cacheManager, "The cacheManager is null;");
        Objects.requireNonNull(cacheProvider, "The cacheProvider is null;");
        Objects.requireNonNull(cacheFactory, "The cacheFactory is null;");
        this.cacheProvider = cacheProvider;
        this.cacheFactory = (AbstractCacheFactory)cacheFactory;
        this.cacheManager = cacheManager;
    }

    @Override
    public final void add(K key, V value) {
        add(key, value, this.expiredSeconds);
    }

    @Override
    public final void add(K key, V value, int expiredSeconds) {
        PreHandleResult result = preHandle(key, value);
        if(!result.wasOk()) {
            return;
        }
        cacheProvider.addToCache(key, value, expiredSeconds);
    }

    @Override
    public final void addAll(Map<K, V> map) {
        addAll(map, this.expiredSeconds);
    }

    @Override
    public final void addAll(Map<K, V> map, int expiredSeconds) {
        PreHandleResult result = preHandle(map);
        if(!result.wasOk()) {
            return;
        }
        cacheProvider.addToCache(map, expiredSeconds);
    }

    @Override
    public final V get(K key) {
        PreHandleResult result = preHandle(key);
        if(!result.wasOk()) {
            return null;
        }
        CacheObject<K, V> cachedObject = cacheProvider.getFromCache(key);
        if (cachedObject.isNull()) {
            cacheManager.incrMiss(this, key);
        } else {
            if(cachedObject.isEmpty()) {
                cacheManager.incrEmptyHit(this, key);
            } else if(cachedObject.getValue() != null) {
                cacheManager.incrHit(this, key);
                tryToExtendExpiredSeconds(key);
            }
        }
        return cachedObject.getValue();
    }

    @Override
    public final List<V> getAll(Collection<K> collection) {
        PreHandleResult result = preHandle(collection);
        if(!result.wasOk()) {
            return new ArrayList<>();
        }
        List<V> list = new ArrayList<>();
        List<CacheObject<K, V>> cachedObjectList
                = cacheProvider.getFromCache(listDistinct(collection));
        Set<K> hitKeys = new HashSet<>();
        Set<K> emptyHitKeys = new HashSet<>();
        Set<K> missKeys = new HashSet<>();
        K key;
        for(CacheObject<K, V> cachedObject : cachedObjectList) {
            key = cachedObject.getKey();
            if (cachedObject.isNull()) {
                missKeys.add(key);
            } else {
                if(cachedObject.isEmpty()) {
                    emptyHitKeys.add(key);
                } else if(cachedObject.getValue() != null) {
                    hitKeys.add(key);
                    list.add(cachedObject.getValue());
                }
            }
        }
        cacheManager.incr(this, hitKeys, emptyHitKeys, missKeys);
        tryToExtendExpiredSeconds(hitKeys);
        return list;
    }

    @Override
    public final Map<K, V> getMap(Collection<K> collection) {
        PreHandleResult result = preHandle(collection);
        if(!result.wasOk()) {
            return new HashMap<>();
        }
        Map<K, V> map = new HashMap<>();
        List<CacheObject<K, V>> cachedObjectList
                = cacheProvider.getFromCache(listDistinct(collection));
        Set<K> hitKeys = new HashSet<>();
        Set<K> emptyHitKeys = new HashSet<>();
        Set<K> missKeys = new HashSet<>();
        K key;
        for(CacheObject<K, V> cachedObject : cachedObjectList) {
            key = cachedObject.getKey();
            if (cachedObject.isNull()) {
                missKeys.add(key);
            } else {
                if(cachedObject.isEmpty()) {
                    emptyHitKeys.add(key);
                } else if(cachedObject.getValue() != null) {
                    hitKeys.add(key);
                    map.put(key, cachedObject.getValue());
                }
            }
        }
        cacheManager.incr(this, hitKeys, emptyHitKeys, missKeys);
        tryToExtendExpiredSeconds(hitKeys);
        return map;
    }

    @Override
    public final V getAndFetch(K key, Function<K, V> fetcher) {
        Objects.requireNonNull(fetcher, "The fetcher is null.");
        PreHandleResult result = preHandle(key);
        if(PreHandleResultEnum.CACHE_DISABLED.equals(result.getType())) {
            return fetcher.apply(key);
        } else if(!result.wasOk()) {
            return null;
        }
        CacheObject<K, V> cachedObject = cacheProvider.getFromCache(key);
        if (cachedObject.isNull()) {
            cacheManager.incrMiss(this, key);
            //从数据获取代理中查找对象，并更新缓存
            cachedObject = fetchObjectWithLock(key, fetcher);
        } else {
            if (cachedObject.isEmpty()) {
                cacheManager.incrEmptyHit(this, key);
            } else if(cachedObject.getValue() != null) {
                cacheManager.incrHit(this, key);
                tryToExtendExpiredSeconds(key);
            }
        }
        return cachedObject.getValue();
    }

    @Override
    public List<V> getAllAndFetch(Collection<K> collection,
                           Function<K, V> fetcher,
                           Supplier<List<V>> multiFetcher) {
        Objects.requireNonNull(fetcher, "The fetcher is null.");
        PreHandleResult result = preHandle(collection);
        if(PreHandleResultEnum.CACHE_DISABLED.equals(result.getType())) {
            if(multiFetcher != null) {
                return multiFetcher.get();
            } else {
                return new ArrayList<>();
            }
        } else if(!result.wasOk()) {
            return new ArrayList<>();
        }
        List<V> list = new ArrayList<>();
        List<CacheObject<K, V>> cachedObjectList
                = cacheProvider.getFromCache(listDistinct(collection));
        Set<K> hitKeys = new HashSet<>();
        Set<K> emptyHitKeys = new HashSet<>();
        Set<K> missKeys = new HashSet<>();
        K key;
        Iterator<CacheObject<K, V>> it = cachedObjectList.iterator();
        while(it.hasNext()) {
            CacheObject<K, V> cachedObject = it.next();
            key = cachedObject.getKey();
            if (cachedObject.isNull()) {
                missKeys.add(key);
                //从数据获取代理中查找对象，并更新缓存
                cachedObject = fetchObjectWithLock(cachedObject.getKey(), fetcher);
            } else {
                if (cachedObject.isEmpty()) {
                    emptyHitKeys.add(key);
                } else if(cachedObject.getValue() != null) {
                    hitKeys.add(key);
                }
            }
            V data = cachedObject.getValue();
            if(data != null) {
                list.add(data);
            }
        }
        cacheManager.incr(this, hitKeys, emptyHitKeys, missKeys);
        tryToExtendExpiredSeconds(hitKeys);
        return list;
    }

    @Override
    public List<V> getAllAndFetch(Collection<K> keyCollection,
                                  Function<Collection<K>, Map<K, V>> delegate) {
        Objects.requireNonNull(delegate, "The delegate is null.");
        Collection<K> distinctKeyCollection = distinctCollection(keyCollection);
        PreHandleResult result = preHandle(distinctKeyCollection);
        if(PreHandleResultEnum.CACHE_DISABLED.equals(result.getType())) {
            List<V> list = new ArrayList<>();
            Map<K, V> map = delegate.apply(distinctKeyCollection);
            if(!CollectionUtils.isEmpty(map)) {
                if(map instanceof LinkedHashMap) {
                    list.addAll(map.values());
                } else {
                    V value;
                    for (K key : distinctKeyCollection) {
                        if ((value = map.get(key)) != null) {
                            list.add(value);
                        }
                    }
                }
            }
            return list;
        } else if(!result.wasOk()) {
            return new ArrayList<>();
        }

        Map<K, CacheObject<K, V>> cachedObjectMap = getCachedObjectMap(distinctKeyCollection,
                delegate);

        List<V> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(cachedObjectMap)) {
            V value;
            for(K key : distinctKeyCollection) {
                CacheObject<K, V> cacheObject = cachedObjectMap.get(key);
                if(cacheObject != null && (value = cacheObject.getValue()) != null) {
                    list.add(value);
                }
            }
        }
        return list;
    }

    protected final Collection<K> distinctCollection(Collection<K> collection) {
        if(CollectionUtils.isEmpty(collection)) {
            return collection;
        }
        return collection.stream().distinct().collect(Collectors.toList());
    }

    private Map<K, CacheObject<K, V>> getCachedObjectMap(Collection<K> keyCollection,
                                                         Function<Collection<K>, Map<K, V>> delegate) {
        Map<K, CacheObject<K, V>> cachedObjectMap = new LinkedHashMap<>();
        Set<K> hitKeys = new HashSet<>();
        Set<K> emptyHitKeys = new HashSet<>();
        Set<K> missKeys = new HashSet<>();
        List<CacheObject<K, V>> cachedObjectList = cacheProvider.getFromCache(keyCollection);
        fill(cachedObjectList, hitKeys, emptyHitKeys, missKeys, cachedObjectMap);
        if(missKeys.size() > 0) {
            fetchObjectWithLock(hitKeys, emptyHitKeys, missKeys, delegate, cachedObjectMap);
        }
        cacheManager.incr(this, hitKeys, emptyHitKeys, missKeys);
        tryToExtendExpiredSeconds(hitKeys);
        return cachedObjectMap;
    }

    private void fill(List<CacheObject<K, V>> cachedObjectList,
                      Set<K> hitKeys,
                      Set<K> emptyHitKeys,
                      Set<K> missKeys,
                      Map<K, CacheObject<K, V>> cachedObjectMap) {
        if(CollectionUtils.isEmpty(cachedObjectList)) {
            return;
        }
        missKeys.clear();
        K key;
        for(CacheObject<K, V> cachedObject : cachedObjectList) {
            key = cachedObject.getKey();
            if (cachedObject.isNull()) {
                missKeys.add(key);
            } else if (cachedObject.isEmpty()) {
                emptyHitKeys.add(key);
                cachedObjectMap.put(key, cachedObject);
            } else if(cachedObject.getValue() != null) {
                hitKeys.add(key);
                cachedObjectMap.put(key, cachedObject);
            }
        }
    }

    protected void fetchObjectWithLock(Set<K> hitKeys,
                                       Set<K> emptyHitKeys,
                                       Set<K> missKeys,
                                       Function<Collection<K>, Map<K, V>> delegate,
                                       Map<K, CacheObject<K, V>> cachedObjectMap) {
        synchronized (this) {
            //加锁防止多线程并发下的重复多次更新缓存
            //再次访问缓存，即double check，防止多线程并发下，多次访问数据库
            List<CacheObject<K, V>> cachedObjectList = cacheProvider.getFromCache(missKeys);
            fill(cachedObjectList, hitKeys, emptyHitKeys, missKeys, cachedObjectMap);
            if(missKeys.size() > 0) {
                //第三次访问缓存，防止多进程并发下，多次访问数据库
                SimpleDisLock<String> simpleDisLock = cacheProvider.getSimpleDisLock();
                if(simpleDisLock != null) {
                    String lockKey = this.name + "mget";
                    try{
                        simpleDisLock.lock(lockKey);
                        cachedObjectList = cacheProvider.getFromCache(missKeys);
                        fill(cachedObjectList, hitKeys, emptyHitKeys, missKeys, cachedObjectMap);
                        if(missKeys.size() > 0) {
                            fetchObject(missKeys, delegate, cachedObjectMap);
                        }
                    } finally {
                        simpleDisLock.unlock(lockKey);
                    }
                } else {
                    fetchObject(missKeys, delegate, cachedObjectMap);
                }
            }
        }
    }

    protected final void fetchObject(Collection<K> keys,
                                     Function<Collection<K>, Map<K, V>> delegate,
                                     Map<K, CacheObject<K, V>> cachedObjectMap) {
        //从代理（实际的数据源，如关系数据库）获取数据
        Map<K, V> valueMap = delegate.apply(keys);
        if(CollectionUtils.isEmpty(valueMap)) {
            for(K key : keys) {
                cachedObjectMap.put(key, createCacheObject(key, null));
            }
        } else {
            for(K key : keys) {
                cachedObjectMap.put(key, createCacheObject(key, valueMap.get(key)));
            }
        }
    }

    private CacheObject<K, V> createCacheObject(K key, V value) {
        CacheObject<K, V> cachedObject;
        if(value != null) {
            cachedObject = new CacheObject<K, V>(key, value);
            addCachedObject(key, cachedObject);
        } else {
            if (this.enableBreakdownPrevent) {
                //空数据处理，防止缓存击穿
                cachedObject = new CacheObject<K, V>(key).asEmpty();
                addCachedObject(key, cachedObject);
            } else {
                cachedObject = new CacheObject<K, V>(key);
            }
        }
        return cachedObject;
    }

    @Override
    public final Map<K, V> getMapAndFetch(Collection<K> collection,
                                         Function<K, V> fetcher,
                                         Supplier<Map<K, V>> multiFetcher) {
        Objects.requireNonNull(fetcher, "The fetcher is null.");
        PreHandleResult result = preHandle(collection);
        if(PreHandleResultEnum.CACHE_DISABLED.equals(result.getType())) {
            if(multiFetcher != null) {
                return multiFetcher.get();
            } else {
                return new HashMap<>();
            }
        } else if(!result.wasOk()) {
            return new HashMap<>();
        }
        Map<K, V> map = new HashMap<>();
        List<CacheObject<K, V>> cachedObjectList
                = cacheProvider.getFromCache(listDistinct(collection));
        Set<K> hitKeys = new HashSet<>();
        Set<K> emptyHitKeys = new HashSet<>();
        Set<K> missKeys = new HashSet<>();
        K key;
        Iterator<CacheObject<K, V>> it = cachedObjectList.iterator();
        while(it.hasNext()) {
            CacheObject<K, V> cachedObject = it.next();
            key = cachedObject.getKey();
            if (cachedObject.isNull()) {
                missKeys.add(key);
                //从数据获取代理中查找对象，并更新缓存
                cachedObject = fetchObjectWithLock(cachedObject.getKey(), fetcher);
            } else {
                if (cachedObject.isEmpty()) {
                    emptyHitKeys.add(key);
                } else if(cachedObject.getValue() != null) {
                    hitKeys.add(key);
                }
            }
            V data = cachedObject.getValue();
            if(data != null) {
                map.put(cachedObject.getKey(), data);
            }
        }
        cacheManager.incr(this, hitKeys, emptyHitKeys, missKeys);
        tryToExtendExpiredSeconds(hitKeys);
        return map;
    }

    @Override
    public final Boolean remove(K key) {
        PreHandleResult result = preHandle(key);
        if(!result.wasOk()) {
            return false;
        }
        return cacheProvider.removeFromCache(key);
    }

    @Override
    public final Long removeAll(Collection<K> collection) {
        PreHandleResult result = preHandle(collection);
        if(!result.wasOk()) {
            return 0L;
        }
        return cacheProvider.removeFromCache(collection);
    }

    @Override
    public final Long getExpire(K key) {
        PreHandleResult result = preHandle(key);
        if(!result.wasOk()) {
            return -1L;
        }
        return cacheProvider.getExpire(key);
    }

    @Override
    public Set<K> getKeys(K pattern, int scanSize) {
        return cacheProvider.getKeys(pattern, scanSize);
    }

    protected final PreHandleResult preHandle(K key) {
        validateKey(key);
        return isAllowed(key);
    }

    protected final PreHandleResult preHandle(K key, V value) {
        validateKey(key);
        validateValue(value);
        return isAllowed(key);
    }

    protected final PreHandleResult preHandle(Collection<K> collection) {
        validateKey(collection);
        return isAllowed(collection);
    }

    protected final PreHandleResult preHandle(Map<K, V> map) {
        validate(map);
        return isAllowed(map.keySet());
    }

    /**
     * 加锁获取缓存对象，子类可以重写此方法，例如当需要引入分布式锁的场景
     *
     * @param key		缓存键
     * @param fetcher	数据获取代理，例如从数据库获取数据
     * @return
     */
    protected CacheObject<K, V> fetchObjectWithLock(K key,
                                                  Function<K, V> fetcher) {
        CacheObject<K, V> cachedObject;
        synchronized (this) {
            //加锁防止多线程并发下的重复多次更新缓存
            //再次访问缓存，即double check，防止多线程并发下，多次访问数据库
            cachedObject = cacheProvider.getFromCache(key);
            if (cachedObject.isNull()) {
                SimpleDisLock<String> simpleDisLock = cacheProvider.getSimpleDisLock();
                if(simpleDisLock != null) {
                    String lockKey = String.valueOf(key);
                    try{
                        simpleDisLock.lock(lockKey);
                        //第三次访问缓存，防止多进程并发下，多次访问数据库
                        cachedObject = cacheProvider.getFromCache(key);
                        if (cachedObject.isNull()) {
                            cachedObject = fetchObject(key, fetcher);
                        } else if (cachedObject.isEmpty()) {
                            cacheManager.incrEmptyHit(this, key);
                        } else if(cachedObject.getValue() != null) {
                            //在并发获得分布式锁后命中缓存，只累计命中次数，不做延长缓存过期时间的操作
                            cacheManager.incrHit(this, key);
                        }
                    } finally {
                        simpleDisLock.unlock(lockKey);
                    }
                } else {
                    cachedObject = fetchObject(key, fetcher);
                }
            } else if (cachedObject.isEmpty()) {
                cacheManager.incrEmptyHit(this, key);
            } else if(cachedObject.getValue() != null) {
                //在并发获得本地锁后命中缓存，只累计命中次数，不做延长缓存过期时间的操作
                cacheManager.incrHit(this, key);
            }
        }
        return cachedObject;
    }

    /**
     * 获取缓存对象
     * @param key		缓存键
     * @param fetcher  	数据获取代理，例如从数据库获取数据
     * @return
     */
    protected final CacheObject<K, V> fetchObject(K key,
                                                Function<K, V> fetcher) {
        CacheObject<K, V> cachedObject = null;
        //从数据库获取数据
        final V data = fetcher.apply(key);
        if(data != null) {
            cachedObject = new CacheObject<K, V>(key, data);
            addCachedObject(key, cachedObject);
        } else {
            if (this.enableBreakdownPrevent) {
                //空数据处理，防止缓存击穿
                cachedObject = new CacheObject<K, V>(key).asEmpty();
                addCachedObject(key, cachedObject);
            } else {
                cachedObject = new CacheObject<K, V>(key);
            }
        }
        return cachedObject;
    }

    private void addCachedObject(K key, CacheObject<K, V> value) {
        if(value.isEmpty()) {
            cacheProvider.addEmptyToCache(key, this.breakdownPreventExpiredSeconds);
        } else {
            cacheProvider.addToCache(key, value.getValue(), this.expiredSeconds);
        }
    }

    private PreHandleResult isAllowed(K key) {
        CachePreHandler cachePreHandler = cacheFactory.getCachePreHandler();
        if(cachePreHandler == null) {
            return new PreHandleResult();
        }
        return cachePreHandler.isAllowed(key);
    }

    private PreHandleResult isAllowed(Collection<K> collection) {
        CachePreHandler cachePreHandler = cacheFactory.getCachePreHandler();
        if(cachePreHandler == null) {
            return new PreHandleResult();
        }
        return cachePreHandler.isAllowed(collection);
    }

    private void validateKey(K key) {
        Objects.requireNonNull(key, "The key is null.");
        if((key instanceof String) && !StringUtils.hasText((String)key)) {
            throw new IllegalArgumentException("The key is empty.");
        }
    }

    private void validateKey(Collection<K> collection) {
        if(collection == null || collection.size() == 0) {
            throw new IllegalArgumentException("The collection is null or empty collection.");
        }
        for(K key : collection) {
            validateKey(key);
        }
    }

    private void validateValue(V value) {
        if(value == null) {
            throw new IllegalArgumentException("The value is null!");
        }
        if(CacheObject.isEmptyValueForBreakdownPrevent(value)) {
            throw new IllegalArgumentException("The value " + value + " is preserved!");
        }
    }

    private void validate(Map<K, V> map) {
        if (map == null || map.size() == 0) {
            throw new IllegalArgumentException("The map is null or empty map.");
        }
        for (Map.Entry<K, V> entry : map.entrySet()) {
            validateKey(entry.getKey());
            validateValue(entry.getValue());
        }
    }

    private void tryToExtendExpiredSeconds(K key) {
        if(enableExtendExpiredSecondsWhenHit) {
            int expiredSeconds = getExpiredSeconds();
            if (expiredSeconds > 0) {
                cacheFactory.asyncExecute(
                        () -> {
                            //延长缓存的生命周期
                            cacheProvider.expire(key, expiredSeconds);
                        });
            }
        }
    }

    private void tryToExtendExpiredSeconds(Collection<K> keys) {
        if(enableExtendExpiredSecondsWhenHit && !CollectionUtils.isEmpty(keys)) {
            int expiredSeconds = getExpiredSeconds();
            if (expiredSeconds > 0) {
                cacheFactory.asyncExecute(
                        () -> {
                            //延长缓存的生命周期
                            cacheProvider.expire(keys, expiredSeconds);
                        });
            }
        }
    }

    private Collection<K> listDistinct(Collection<K> collection) {
        if(CollectionUtils.isEmpty(collection)) {
            return collection;
        }
        return new HashSet<>(collection);
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final void setName(String name) {
        if (name == null || "".equals(name.trim())) {
            throw new IllegalArgumentException("The name should not be null or empty.");
        }
        this.name = name;
    }

    @Override
    public final String getDescription() {
        return description;
    }

    @Override
    public final void setDescription(String description) {
        this.description = description;
    }

    @Override
    public final int getExpiredSeconds() {
        return expiredSeconds;
    }

    @Override
    public final void setExpiredSeconds(int expiredSeconds) {
        this.expiredSeconds = expiredSeconds;
    }

    @Override
    public void setEnableBreakdownPrevent(boolean enableBreakdownPrevent) {
        this.enableBreakdownPrevent = enableBreakdownPrevent;
    }

    @Override
    public boolean isEnableBreakdownPrevent() {
        return enableBreakdownPrevent;
    }

    @Override
    public void setBreakdownPreventExpiredSeconds(int seconds) {
        this.breakdownPreventExpiredSeconds = seconds;
    }

    @Override
    public int getBreakdownPreventExpiredSeconds() {
        return breakdownPreventExpiredSeconds;
    }

    @Override
    public boolean isEnableExtendExpiredSecondsWhenHit() {
        return enableExtendExpiredSecondsWhenHit;
    }

    @Override
    public void setEnableExtendExpiredSecondsWhenHit(boolean enableExtendExpiredSecondsWhenHit) {
        this.enableExtendExpiredSecondsWhenHit = enableExtendExpiredSecondsWhenHit;
    }

    @Override
    public CacheProvider<K, V> getCacheProvider() {
        return this.cacheProvider;
    }
}
