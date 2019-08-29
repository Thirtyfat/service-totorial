package com.mglj.totorial.framework.tools.model.tuples;


public class Pair<T1, T2> extends Single<T1> {

    protected T2 item2;

    public Pair() {}

    public Pair(T1 item1, T2 item2) {
        super(item1);
        this.item2 = item2;
    }

    public final T2 getItem2() {
        return item2;
    }

    public void setItem2(T2 item2) {
        this.item2 = item2;
    }

}
