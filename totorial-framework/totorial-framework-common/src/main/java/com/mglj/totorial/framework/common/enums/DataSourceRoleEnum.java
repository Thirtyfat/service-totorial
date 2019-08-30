package com.mglj.totorial.framework.common.enums;


import com.mglj.totorial.framework.common.lang.CodeEnum;

/**
 * 数据源类型
 *
 * @author zsp
 * @date 2016-11-24
 */
public enum DataSourceRoleEnum implements CodeEnum {

    /**
     * 可写的数据源
     */
    WRITABLE(0),
    /**
     * 只读数据源
     */
    READ_ONLY(1);

    private final int code;

    private DataSourceRoleEnum(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }


}
