package com.mglj.totorial.framework.tool.concurrent.locks;

/**
 * 一个简单的分布式锁。
 *
 * Created by zsp on 2018/8/22.
 */
public interface SimpleDisLock<E> {

    /**
     * 获取锁，返回true表示成功获取到锁，否则返回false表示获取锁失败。
     *
     * @param object            被锁的对象。
     * @return
     */
    boolean lock(E object);

    /**
     * 获取锁，返回true表示成功获取到锁，否则返回false表示获取锁失败。
     *
     * @param object            被锁的对象。
     * @param timeoutMillis     尝试获取锁而等待最大的时间（毫秒）
     * @return
     */
    boolean tryLock(E object, long timeoutMillis);

    /**
     * 释放锁。
     *
     * @param object            被锁的对象。
     */
    void unlock(E object);

}
