package com.mglj.totorial.framework.tool.data.redis.locks;

import com.mglj.totorial.framework.tool.concurrent.locks.SimpleDisLock;
import com.mglj.totorial.framework.tool.data.redis.StringOperationTemplate;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.Objects;

/**
 * 基于redis的简单分布式锁实现。
 *
 * Created by zsp on 2018/8/21.
 */
public class RedisSimpleDisLock implements SimpleDisLock<String> {

    private final static int DEFAULT_ACQUIRE_RESOLUTION_MILLIS = 100;
    private final static String LOCK_POSTFIX = "_lock";
    private final static String TASK_POSTFIX = "_task";

    private final StringOperationTemplate<String, String> stringTemplate;
    private final String distributedId;
    private final LockHolder lockHolder;
    private final SecureRandom random = new SecureRandom();

    private long expireMillis = 60 * 1000;

    public long getExpireMillis() {
        return expireMillis;
    }

    public void setExpireMillis(long expireMillis) {
        if(expireMillis < 1) {
            throw new IndexOutOfBoundsException("The expireMillis should be than 0.");
        }
        this.expireMillis = expireMillis;
    }

    /**
     * 构建一个分布式锁。
     *
     * @param stringTemplate
     * @param distributedId
     */
    public RedisSimpleDisLock(StringOperationTemplate<String, String> stringTemplate,
                              String distributedId) {
        this(stringTemplate, distributedId, 1);
    }

    /**
     * 构建一个分布式锁。
     *
     * @param stringTemplate
     * @param distributedId
     * @param maxConcurrentLockCount       最大并发锁的个数
     */
    public RedisSimpleDisLock(StringOperationTemplate<String, String> stringTemplate,
                              String distributedId,
                              int maxConcurrentLockCount) {
        Objects.requireNonNull(stringTemplate, "The stringTemplate is null.");
        if(!StringUtils.hasText(distributedId)) {
            throw new NullPointerException("The distributedId is null or empty.");
        }
        this.stringTemplate = stringTemplate;
        this.distributedId = distributedId;
        if(maxConcurrentLockCount < 1) {
            maxConcurrentLockCount = 1;
        }
        this.lockHolder = new LockHolder(maxConcurrentLockCount);
    }

    public boolean lock(String object) {
        return tryLock(object, 0);
    }

    public boolean tryLock(String object, long timeoutMillis) {
        Objects.requireNonNull(object);
        String key = getKey(object);
        String taskKey = getTaskKey(object);
        long timeout = timeoutMillis;
        synchronized (lockHolder.getLockObject(object)) {
            for(;;) {
                String expireTime = String.valueOf(System.currentTimeMillis() + expireMillis + 1);
                if(stringTemplate.setIfAbsent(key, expireTime)) {
                    //成功设置key的值为一个时间戳，表示成功得到锁，则直接返回锁
                    stringTemplate.set(taskKey, getTaskId());
                    return true;
                }
                // 没有拿到锁，则获取key的时间戳，判断其是否小于当前系统时间？如果是，说明锁已经过期，则通过redis
                // 的getset方法设置新的时间戳，并返回旧的时间戳；如果旧的时间戳小于当前系统时间，则成功得到锁，
                // 否则可能是其它线程先获取了锁并设置了新的时间戳；在并发的情况下，当前线程即使没有成功获取锁，但是
                // 修改了时间戳且多计数了，考虑到并发情况下先后时间比较短，可忽略不考虑。
                String expireTimeFound = stringTemplate.get(key);
                if(expireTimeFound == null || Long.parseLong(expireTimeFound) < System.currentTimeMillis()) {
                    expireTime = String.valueOf(System.currentTimeMillis() + expireMillis + 1);
                    String oldExpireTime = stringTemplate.getAndSet(key, expireTime);
                    if(oldExpireTime == null || Long.parseLong(oldExpireTime) < System.currentTimeMillis()) {
                        stringTemplate.set(taskKey, getTaskId());
                        return true;
                    }
                }
                long delta = DEFAULT_ACQUIRE_RESOLUTION_MILLIS - random.nextInt(DEFAULT_ACQUIRE_RESOLUTION_MILLIS);
                timeout -= delta;
                if(timeout < 1) {
                    break;
                }
                //休眠一定时间，继续尝试获取锁
                try {
                    Thread.sleep(delta);
                } catch (InterruptedException e) {
                    //do nothing
                }
            }
            return false;
        }
    }

    public void unlock(String object) {
        Objects.requireNonNull(object);
        String key = getKey(object);
        String taskKey = getTaskKey(object);
        synchronized (lockHolder.getLockObject(object)) {
            //为了防止误删别的任务的锁，需要根据任务标识来判断锁是否是当前任务的。
            String taskId = stringTemplate.get(taskKey);
            if (Objects.equals(taskId, getTaskId())) {
                stringTemplate.delete(key);
                stringTemplate.delete(taskKey);
            }
        }
    }

    private String getKey(String lockKey) {
        return lockKey + LOCK_POSTFIX;
    }

    private String getTaskKey(String lockKey) {
        return lockKey + TASK_POSTFIX;
    }

    private String getTaskId() {
        //任务由分布式ID（机器+进程组合识别）+线程ID来标识
        return distributedId + "_" + Thread.currentThread().getId();
    }

    /**
     * 本地锁持有者，用于控制锁的粒度。
     * 如果一个key一把锁，锁的个数会特别多；所有key对应一把锁，锁的粒度太粗。
     * 可以固定锁的个数，根据key的hashcode来计算返回确定的锁，一方面细化了锁，一方面避免过多的锁对象。
     */
    private static class LockHolder {
        private int size;
        private Object[] lockObjects;

        LockHolder(int size) {
            this.size = size;
            lockObjects = new Object[size];
            for(int i = 0; i < size; i++){
                lockObjects[i] = new Object();
            }
        }

        private Object getLockObject(String lockKey) {
            if(size == 1) {
                return lockObjects[0];
            }
            return lockObjects[Math.abs(lockKey.hashCode() % size)];
        }
    }

}
