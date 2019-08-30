package com.mglj.totorial.framework.tool.data.redis.hash;

import java.util.Objects;

/**
 * Created by zsp on 2018/8/21.
 */
public abstract class AbstractBucketLocator<K> implements BucketLocator<K> {

    protected int size;
    public int getSize() {
        return size;
    }

    public AbstractBucketLocator() {
        this.size = 16;
    }

    public abstract int getBucket(K key);

    @Override
    public String getIndex(K key) {
        Objects.requireNonNull(key);
        return String.valueOf(key);
    }

}
