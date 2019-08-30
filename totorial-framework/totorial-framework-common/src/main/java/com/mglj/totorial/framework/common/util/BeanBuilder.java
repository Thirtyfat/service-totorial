package com.mglj.totorial.framework.common.util;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Created by zsp on 2018/8/14.
 */
public class BeanBuilder<T> {

    protected final T bean;

    public BeanBuilder(T bean) {
        Objects.requireNonNull(bean, "The bean is null.");
        this.bean = bean;
    }

    public BeanBuilder(Supplier<T> factory) {
        Objects.requireNonNull(factory, "The factory is null.");
        T bean0 = factory.get();
        Objects.requireNonNull(bean0, "The bean is null.");
        this.bean = bean0;
    }

    public final T get() {
        return bean;
    }

}
