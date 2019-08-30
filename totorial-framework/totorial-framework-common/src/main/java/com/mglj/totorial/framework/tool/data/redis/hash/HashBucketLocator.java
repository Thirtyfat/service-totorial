package com.mglj.totorial.framework.tool.data.redis.hash;

import java.util.Objects;

/**
 * Created by zsp on 2018/8/21.
 */
public class HashBucketLocator <K> extends AbstractBucketLocator<K> {

    public HashBucketLocator() {
        super();
    }

    public HashBucketLocator(int bucketCount) {
        if(bucketCount < 1) {
            throw new IndexOutOfBoundsException("The bucketCount should be than 0.");
        }
        this.size = bucketCount;
    }

    @Override
    public int getBucket(K key) {
        Objects.requireNonNull(key);
        return Math.abs(key.hashCode()) % size;
    }
}
