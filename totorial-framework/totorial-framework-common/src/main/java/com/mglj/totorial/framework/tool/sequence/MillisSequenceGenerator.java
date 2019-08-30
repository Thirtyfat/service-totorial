package com.mglj.totorial.framework.tool.sequence;

/**
 * Created by zsp on 2018/9/21.
 */
public class MillisSequenceGenerator implements SequenceGenerator {
    @Override
    public long generate() {
        return System.currentTimeMillis();
    }
}
