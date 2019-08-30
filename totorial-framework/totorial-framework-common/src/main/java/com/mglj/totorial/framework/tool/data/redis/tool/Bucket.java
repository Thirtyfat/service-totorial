package com.mglj.totorial.framework.tool.data.redis.tool;

/**
 * Created by zsp on 2018/9/20.
 */
public class Bucket<T> {

    private int index;
    private T value;

    public Bucket(int index, T value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
