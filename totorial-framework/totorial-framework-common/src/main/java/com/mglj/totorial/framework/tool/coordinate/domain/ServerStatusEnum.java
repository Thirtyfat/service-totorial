package com.mglj.totorial.framework.tool.coordinate.domain;


import com.mglj.totorial.framework.common.lang.CodeEnum;

/**
 * Created by zsp on 2018/7/13.
 */
public enum ServerStatusEnum implements CodeEnum<ServerStatusEnum> {

    NONE(0),
    INACTIVE(1),
    ACTIVE(2),
    REMOVED(3);

    private int code;

    private ServerStatusEnum(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

}
