package com.mglj.totorial.framework.common.lang;

/**
 * Created by zsp on 2018/11/20.
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
