package com.mglj.totorial.framework.tools.model;

/**
 * Created by yj on 2018/11/20.
 */
public class IntegerHolder extends Holder<Integer> {

    public IntegerHolder() {
        super();
    }

    public IntegerHolder(Integer value) {
        super(value);
    }

    public void increment(int delta) {
        if(!isPresent()) {
            this.value = 0;
        }
        this.value += delta;
    }

}
