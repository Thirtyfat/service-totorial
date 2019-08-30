package com.mglj.totorial.framework.tool.data.redis.hash;

/**
 * Created by zsp on 2018/8/21.
 */
public interface BucketLocator<K> {

    int getBucket(K key);

    String getIndex(K key);

}
