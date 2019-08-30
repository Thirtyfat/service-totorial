package com.mglj.totorial.framework.common.enums;


import com.mglj.totorial.framework.common.lang.CodeEnum;

/**
 * 使用情况枚举
 *
 * Created by zsp on 2018/8/24.
 */
public enum UsedEnum implements CodeEnum {

    /**
     * 存盘/草稿
     */
    DRAFT(0),
    /**
     * 启用
     */
    ENABLE(1),
    /**
     * 停用
     */
    DISABLE(2);

    private int code;

    private UsedEnum(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

}
