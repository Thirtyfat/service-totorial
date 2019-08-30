package com.mglj.totorial.framework.common.function;

import java.util.Map;

/**
 * Created by zsp on 2018/11/20.
 */
@FunctionalInterface
public interface ForeachSubMapConsumer<K, V> {

    void foreach(int loop, int startIndex, int endIndex, Map<K, V> subMap);

}
