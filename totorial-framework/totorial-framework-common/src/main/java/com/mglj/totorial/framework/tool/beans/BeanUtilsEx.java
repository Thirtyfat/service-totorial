package com.mglj.totorial.framework.tool.beans;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Supplier;

/**
 * Created by zsp on 2018/8/7.
 */
public class BeanUtilsEx {

    /**
     *
     * @param source
     * @param target
     * @param <S>
     * @param <T>
     */
    public static <S, T> void copyProperties(S source, T target) {
        Objects.requireNonNull(source, "The source is required.");
        Objects.requireNonNull(target, "The target is required.");
        BeanUtils.copyProperties(source, target);
    }

    /**
     *
     * @param source
     * @param creator
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> T copyProperties(S source, Supplier<T> creator) {
        Objects.requireNonNull(source, "The source is required.");
        Objects.requireNonNull(creator, "The creator is required.");
        T target = creator.get();
        Objects.requireNonNull(target, "The target is required.");
        BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     *
     * @param sourceList
     * @param creator
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> List<T> copyPropertiesForNewList(Collection<S> sourceList, Supplier<T> creator) {
        if(CollectionUtils.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        Objects.requireNonNull(creator, "The creator is required.");
        List<T> targetList = new ArrayList<>();
        for(S source : sourceList) {
            targetList.add(copyProperties(source, creator));
        }
        return targetList;
    }

    /**
     *
     * @param sourceList
     * @param creator
     * @param <S>
     * @param <T>
     */
    public static <S, T> void copyPropertiesForList(List<S> sourceList, Supplier<List<T>> creator) {
        if(CollectionUtils.isEmpty(sourceList)) {
            return;
        }
        Objects.requireNonNull(creator, "The creator is required.");
        List<T> targetList = creator.get();
        if(CollectionUtils.isEmpty(targetList)) {
            return;
        }
        int size = sourceList.size() > targetList.size() ? targetList.size() : sourceList.size();
        for(int i = 0; i < size; i++) {
            S source = sourceList.get(i);
            Objects.requireNonNull(source, "The source is required.");
            T target = targetList.get(i);
            Objects.requireNonNull(target, "The target is required.");
            BeanUtils.copyProperties(source, target);
        }
    }

    /**
     *
     * @param sourceSet
     * @param creator
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> Set<T> copyPropertiesForNewSet(Set<S> sourceSet, Supplier<T> creator) {
        if(CollectionUtils.isEmpty(sourceSet)) {
            throw new NullPointerException("The sourceSet is required.");
        }
        Objects.requireNonNull(creator, "The creator is required.");
        Set<T> targetSet = new HashSet<>();
        for(S source : sourceSet) {
            targetSet.add(copyProperties(source, creator));
        }
        return targetSet;
    }

}
