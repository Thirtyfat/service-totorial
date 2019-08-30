package com.mglj.totorial.framework.support.domain.request;


import com.mglj.totorial.framework.common.validation.AlertMessage;

import javax.validation.constraints.NotBlank;

/**
 * Created by zsp on 2018/11/7.
 */
public class CacheRequest {

    @NotBlank(message = "name-required")
    @AlertMessage(code = "name-required")
    private String name;

    private String key;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

}
