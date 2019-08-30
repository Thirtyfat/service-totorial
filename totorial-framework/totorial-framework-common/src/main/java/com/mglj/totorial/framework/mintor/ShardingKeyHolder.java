package com.mglj.totorial.framework.mintor;

/**
 * 分片键，用于线程本地变量的存储
 */
public final class ShardingKeyHolder {

    private static final ThreadLocal<Object> holder = new ThreadLocal<>();

    /**
     *
     * @param shardingKey
     */
    public static void set(Object shardingKey) {
        holder.set(shardingKey);
    }

    /**
     *
     * @return
     */
    public static Object get() {
        return holder.get();
    }

    /**
     *
     */
    public static void clear() {
        holder.remove();
    }

}
