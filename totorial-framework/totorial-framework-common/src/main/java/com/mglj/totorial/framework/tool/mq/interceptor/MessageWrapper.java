package com.mglj.totorial.framework.tool.mq.interceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zsp on 2018/9/4.
 */
public class MessageWrapper<T> {

    private final T message;
    private long sequence;
    private final Map<String, Object> properties = new HashMap<>();

    public MessageWrapper(T message, long sequence) {
        this.message = message;
        this.sequence = sequence;
    }

    public T getMessage() {
        return message;
    }

    public long getSequence() {
        return sequence;
    }

    public void addProperty(String key, Object value) {
        properties.put(key, value);
    }

    public void removeProperty(String key) {
        properties.remove(key);
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

}
