package com.mglj.totorial.framework.common.function;

import java.util.List;

/**
 * Created by zsp on 2018/8/9.
 */
@FunctionalInterface
public interface ForeachSubListConsumer<E> {

    void foreach(int loop, int startIndex, int endIndex, List<E> subList);

}
