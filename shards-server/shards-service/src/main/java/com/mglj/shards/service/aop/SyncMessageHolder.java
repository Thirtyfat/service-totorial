package com.mglj.shards.service.aop;


import com.mglj.shards.service.domain.SyncMessage;

/**
 * Created by zsp on 2019/3/20.
 */
public class SyncMessageHolder {

    private static ThreadLocal<SyncMessage> holder = new ThreadLocal<>();

    public static void set(SyncMessage message) {
        holder.set(message);
    }

    public static SyncMessage get() {
        return holder.get();
    }

    public static void clear() {
        holder.remove();
    }

}
