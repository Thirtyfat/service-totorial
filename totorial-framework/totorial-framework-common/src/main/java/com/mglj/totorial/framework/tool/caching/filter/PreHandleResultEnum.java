package com.mglj.totorial.framework.tool.caching.filter;


import com.mglj.totorial.framework.common.lang.CodeEnum;

/**
 * Created by zsp on 2018/10/26.
 */
public enum PreHandleResultEnum implements CodeEnum {

    OK(0),
    CACHE_DISABLED(1),
    KEY_INVALID(2),
    KEY_NOT_EXISTED(3);

    private int code;

    private PreHandleResultEnum(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

}
