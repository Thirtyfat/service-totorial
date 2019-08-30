package com.mglj.shards.service.domain;

/**
 * Created by zsp on 2019/3/8.
 */
public class CriteriaConfig {

    /**
     * 键
     */
    private String key;
    /**
     * 值
     */
    private String value;
    /**
     * 备注
     */
    private String remark;

    /**
     * 设置键
     */
    public void setKey(String value) {
        this.key = value;
    }
    /**
     * 获取键
     */
    public String getKey() {
        return this.key;
    }
    /**
     * 设置值
     */
    public void setValue(String value) {
        this.value = value;
    }
    /**
     * 获取值
     */
    public String getValue() {
        return this.value;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
