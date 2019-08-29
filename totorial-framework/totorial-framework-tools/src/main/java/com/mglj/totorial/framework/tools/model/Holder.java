package com.mglj.totorial.framework.tools.model;

/**
 * Created by yj on 2018/9/30.
 */
public class Holder<T> {

    protected T value;

    public Holder() {

    }

    public Holder(T value) {
        this.value = value;
    }

    public static <T> Holder<T> create() {
        return new Holder<>();
    }

    public static <T> Holder<T> create(T value) {
        return new Holder<>(value);
    }

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return this.value;
    }

    public boolean isPresent() {
        return value != null;
    }

    @Override
    public String toString() {
        return "Holder{" +
                "value=" + value +
                '}';
    }

}
