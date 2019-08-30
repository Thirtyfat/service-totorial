package com.mglj.totorial.framework.tool.data.redis.hash;

import java.util.Objects;

/**
 *
 * 比如数据ID为100,101...120
 *
 * Created by zsp on 2018/8/21.
 */
public class SerialNumberBucketLocator<K extends Number> extends AbstractBucketLocator<K> {

    public SerialNumberBucketLocator() {
        this.size = 64;
    }

    public SerialNumberBucketLocator(int bucketSize) {
        if(bucketSize < 1) {
            throw new IndexOutOfBoundsException("The bucketSize should be than 0.");
        }
        this.size = bucketSize;
    }

    @Override
    public int getBucket(K key) {
        Objects.requireNonNull(key);
        Number number = (Number)key;
        return number.intValue() / size;
    }

    @Override
    public String getIndex(K key) {
        Objects.requireNonNull(key);
        Number number = (Number)key;
        return String.valueOf(number.intValue() % size);
    }

}
