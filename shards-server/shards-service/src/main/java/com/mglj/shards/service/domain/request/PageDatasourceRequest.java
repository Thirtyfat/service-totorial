package com.mglj.shards.service.domain.request;


import com.mglj.totorial.framework.common.lang.PageRequest;

/**
 * Created by zsp on 2019/3/11.
 */
public class PageDatasourceRequest extends PageRequest {

    /**
     * 类型 0是写，1是读
     */
    private Integer type;
    /**
     * 名称
     */
    private String name;

    /**
     * 设置类型 1是写，2是读
     */
    public void setType(Integer value) {
        this.type = value;
    }
    /**
     * 获取类型 1是写，2是读
     */
    public Integer getType() {
        return this.type;
    }
    /**
     * 设置名称
     */
    public void setName(String value) {
        this.name = value;
    }
    /**
     * 获取名称
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "PageDatasourceRequest{" +
                "offset=" + offset +
                ", type=" + type +
                ", rows=" + rows +
                ", name='" + name + '\'' +
                '}';
    }
}
