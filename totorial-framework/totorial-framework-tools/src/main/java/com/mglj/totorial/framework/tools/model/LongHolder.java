package com.mglj.totorial.framework.tools.model;

/**
 * Created by yj on 2018/11/20.
 */
public class LongHolder extends Holder<Long> {

    public LongHolder() {
        super();
    }

    public LongHolder(Long value) {
        super(value);
    }

    public void increment(long delta) {
        if(!isPresent()) {
            this.value = 0L;
        }
        this.value += delta;
    }

}
