package com.mglj.totorial.framework.tool.data.redis.tool;

/**
 * Created by zsp on 2018/9/20.
 */
public class DirectAccessResponse<T> {

    private T value;
    private Long expiredSeconds;

    public DirectAccessResponse(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Long getExpiredSeconds() {
        return expiredSeconds;
    }

    public void setExpiredSeconds(Long expiredSeconds) {
        this.expiredSeconds = expiredSeconds;
    }

    @Override
    public String toString() {
        return "DirectAccessResponse{" +
                "value=" + value +
                ", expiredSeconds=" + expiredSeconds +
                '}';
    }
}
