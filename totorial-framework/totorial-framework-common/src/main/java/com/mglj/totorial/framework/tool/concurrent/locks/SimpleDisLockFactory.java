package com.mglj.totorial.framework.tool.concurrent.locks;

/**
 * 分布式锁工厂
 *
 * Created by zsp on 2018/9/19.
 */
public interface SimpleDisLockFactory<E> {

    /**
     * 根据令牌获取分布式锁
     *
     * @param token     令牌
     * @return
     */
    SimpleDisLock<E> getSimpleDisLock(SimpleDisLockToken token);

}
