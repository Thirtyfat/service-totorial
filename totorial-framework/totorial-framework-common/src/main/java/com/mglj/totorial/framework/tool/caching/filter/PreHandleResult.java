package com.mglj.totorial.framework.tool.caching.filter;

/**
 * Created by zsp on 2018/10/26.
 */
public class PreHandleResult {

    private PreHandleResultEnum type = PreHandleResultEnum.OK;

    public PreHandleResult() {

    }

    public PreHandleResult(PreHandleResultEnum type) {
        this.type = type;
    }

    public PreHandleResultEnum getType() {
        return type;
    }

    public boolean wasOk() {
        return PreHandleResultEnum.OK.equals(type);
    }
}
