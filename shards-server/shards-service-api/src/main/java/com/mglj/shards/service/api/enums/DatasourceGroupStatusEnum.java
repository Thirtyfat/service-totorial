package com.mglj.shards.service.api.enums;


import com.mglj.totorial.framework.common.lang.CodeEnum;

/**
 * Created by zsp on 2019/3/11.
 */
public enum DatasourceGroupStatusEnum implements CodeEnum {

    /**
     * 关闭
     */
    DISABLE(0),
    /**
     * 启用
     */
    ENABLE(1);

    private int code;

    private DatasourceGroupStatusEnum(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

}
