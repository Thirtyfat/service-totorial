package com.mglj.totorial.framework.tools.function;

import java.util.Map;

/**
 * Created by yj on 2018/11/20.
 */
@FunctionalInterface
public interface ForeachSubMapConsumer<K, V> {

    void foreach(int loop, int startIndex, int endIndex, Map<K, V> subMap);

}
