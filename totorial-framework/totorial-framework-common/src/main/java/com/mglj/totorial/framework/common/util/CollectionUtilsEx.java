package com.mglj.totorial.framework.common.util;


import com.mglj.totorial.framework.common.function.ForeachSubListConsumer;
import com.mglj.totorial.framework.common.function.ForeachSubMapConsumer;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * extension functions for collection utilities
 *
 * Created by zsp on 2018/7/9.
 */
public class CollectionUtilsEx {

    public static final int DEFAULT_BATCH_VISIT_SIZE = 100;

    public static <TKey, TValue> void putListToMap(Map<TKey, List<TValue>> map, TKey key,
                                                   TValue value) {
        if(value == null) {
            return;
        }
        getListFromMap(map, key).add(value);
    }

    public static <TKey, TValue> void putListToMap(Map<TKey, List<TValue>> map, TKey key,
                                                   Collection<TValue> collection) {
        if(isEmpty(collection)) {
            return;
        }
        getListFromMap(map, key).addAll(collection);
    }

    public static <TKey, TValue> List<TValue> getListFromMap(Map<TKey, List<TValue>> map, TKey key) {
        Objects.requireNonNull(map);
        List<TValue> list = map.get(key);
        if(list == null) {
            list = new ArrayList<>();
            map.put(key, list);
        }
        return list;
    }

    public static <TType> boolean isEmpty(Collection<TType> col) {
        return col == null || col.size() == 0;
    }

    public static  <TType> boolean isNotEmpty(Collection<TType> col) {
        return !isEmpty(col);
    }

    public static <TType> boolean isEmpty(TType... array) {
        return array == null || array.length == 0;
    }

    public static  <TType> boolean isNotEmpty(TType... array) {
        return !isEmpty(array);
    }

    /**
     *
     */
    public static <TType, TField> Set<TField> listField(Collection<TType> col,
                                                        Function<TType, TField> fieldFetcher) {
        return listField(col, fieldFetcher, new Predicate[0]);
    }

    /**
     *
     */
    public static <TType, TField> Set<TField> listField(Collection<TType> col,
                                                        Function<TType, TField> fieldFetcher,
                                                        Predicate<TType>... filters) {
        Stream<TType> stream = buildStreamForlistField(col, fieldFetcher, filters);
        return stream.map(fieldFetcher).distinct().collect(Collectors.toSet());
    }

    /**
     *
     */
    public static <TType, TField> Collection<TField> listField(Collection<TType> col,
                                                         Function<TType, TField> fieldFetcher,
                                                         boolean distinct) {
        return listField(col, fieldFetcher, distinct, new Predicate[0]);
    }

    /**
     *
     */
    public static <TType, TField> Collection<TField> listField(Collection<TType> col,
                                                         Function<TType, TField> fieldFetcher,
                                                         boolean distinct,
                                                         Predicate<TType>... filters) {
        Stream<TType> stream = buildStreamForlistField(col, fieldFetcher, filters);
        if (distinct) {
            return stream.map(fieldFetcher).distinct().collect(Collectors.toSet());
        } else {
            return stream.map(fieldFetcher).collect(Collectors.toList());
        }
    }

    private static <TType, TField> Stream<TType> buildStreamForlistField(Collection<TType> col,
                           Function<TType, TField> fieldFetcher,
                           Predicate<TType>... filters) {
        Objects.requireNonNull(col, "The col is null.");
        Objects.requireNonNull(fieldFetcher, "The fieldFetcher is null.");
        Stream<TType> stream = col.stream();
        if (isNotEmpty(filters)) {
            for(Predicate<TType> filter : filters) {
                stream.filter(filter);
            }
        }
        return stream;
    }

    public static <E> void foreachSubList(List<E> list, int batchSize, ForeachSubListConsumer<E> delegate) {
        if(isEmpty(list)) {
            return;
        }
        Objects.requireNonNull(delegate, "The delegate is null.");
        if(batchSize < 1) {
            throw new IllegalArgumentException("The batchSize should be than 1.");
        }
        int loop = 0, startIndex = 0, len = list.size();
        for(; startIndex < len; loop++, startIndex += batchSize) {
            int endIndex = (startIndex + batchSize) > len ? len : startIndex + batchSize;
            List<E> subList = list.subList(startIndex, endIndex);
            delegate.foreach(loop, startIndex, endIndex, subList);
        }
    }

    public static <K, V> void foreachSubMap(Map<K, V> map, int batchSize, ForeachSubMapConsumer<K, V> delegate) {
        if(isEmpty(map)) {
            return;
        }
        Objects.requireNonNull(delegate, "The delegate is null.");
        if(batchSize < 1) {
            throw new IllegalArgumentException("The batchSize should be than 1.");
        }
        List<K> keyList = new ArrayList<>(map.keySet());
        List<K> subKeyList;
        Map<K, V> subMap;
        int loop = 0, startIndex = 0, len = keyList.size();
        for(; startIndex < len; loop++, startIndex += batchSize) {
            int endIndex = (startIndex + batchSize) > len ? len : startIndex + batchSize;
            subKeyList = keyList.subList(startIndex, endIndex);
            subMap = new HashMap<>();
            for(K key : subKeyList) {
                subMap.put(key, map.get(key));
            }
            delegate.foreach(loop, startIndex, endIndex, subMap);
        }
    }

    public static <E> List<E> batchFetchList(int batchSize, Function<Integer, List<E>> delegate) {
        return batchFetchList(batchSize, delegate,
                (subList) -> {
                    return isNotEmpty(subList);
                });
    }

    public static <E> List<E> batchFetchList(int batchSize,
                                             Function<Integer, List<E>> delegate,
                                             Predicate<List<E>> validator) {
        List<E> list = new ArrayList<>();
        for(int loop = 0; ;loop++) {
            List<E> subList = delegate.apply(loop);
            if(validator.test(subList)) {
                list.addAll(subList);
            }
            if(!validator.test(subList) || subList.size() < batchSize) {
                break;
            }
        }
        return list;
    }

    public static boolean batchForeach(Function<Integer, Boolean> delegate) {
        boolean affected = false;
        for(int loop = 0; ;loop++) {
            Boolean flag = delegate.apply(loop);
            if(flag == null || !flag) {
                break;
            }
            affected = true;
        }
        return affected;
    }

    /**
     * 根据第二个集合分支第一个集合，返回三个集合分支：
     * 包括交集（相同的元素）、增集（增加的元素）、差集（删除的元素）
     *
     * @param 	source 			第一个集合
     * @param 	target			第二个集合
     * @return 顺序返回3个列表集合：交集、增集、差集。
     */
    public static <T> List<List<T>> fork(List<T> source, List<T> target) {
        return fork(source, target, null);
    }

    /**
     * 根据第二个集合分支第一个集合，返回三个集合分支：
     * 包括交集（相同的元素）、增集（增加的元素）、差集（删除的元素）
     *
     * @param 	source 			第一个集合
     * @param 	target			第二个集合
     * @param 	classifier		获取集合元素标识字段（例如ID）的代理方法
     * @return 顺序返回3个列表集合：交集、增集、差集。
     */
    public static <T, K> List<List<T>> fork(List<T> source, List<T> target, Function<T, K> classifier) {
        if(source == null || source.size() == 0) {
            throw new NullPointerException("The source is null.");
        }
        Function<T, K> classifier0 = (classifier == null) ? (e) -> (K) e : classifier;

        List<T> intersectionList = new ArrayList<>();
        List<T> addList = new ArrayList<>();
        if (target == null || target.size() == 0) {
            return Arrays.asList(intersectionList, addList, source);
        }
        List<T> subtractList = new ArrayList<>();
        Map<K, T> map = source.stream().collect(Collectors.toMap(classifier0, item -> item));
        for (T element : target) {
            K key = classifier0.apply(element);
            if (map.containsKey(key)) {
                intersectionList.add(element);
            } else {
                addList.add(element);
            }
            map.remove(key);
        }
        if (map.size() > 0) {
            subtractList.addAll(map.values());
        }

        return Arrays.asList(intersectionList, addList, subtractList);
    }

}
