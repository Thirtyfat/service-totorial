package com.mglj.totorial.framework.common.enums;


import com.mglj.totorial.framework.common.lang.CodeEnum;

/**
 * Created by zsp on 2018/7/9.
 */
public enum TrueFalseEnum implements CodeEnum {

    /**
     * false
     */
    FALSE(0),
    /**
     * true
     */
    TRUE(1);

    private int code;

    private TrueFalseEnum(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

}
