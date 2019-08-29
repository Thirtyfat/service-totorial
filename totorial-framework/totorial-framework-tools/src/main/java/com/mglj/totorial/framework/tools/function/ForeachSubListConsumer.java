package com.mglj.totorial.framework.tools.function;

import java.util.List;

/**
 * Created by yj on 2018/8/9.
 */
@FunctionalInterface
public interface ForeachSubListConsumer<E> {

    void foreach(int loop, int startIndex, int endIndex, List<E> subList);

}
