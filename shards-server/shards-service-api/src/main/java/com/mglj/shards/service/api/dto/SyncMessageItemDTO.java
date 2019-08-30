package com.mglj.shards.service.api.dto;

/**
 * Created by zsp on 2019/3/21.
 */
public class SyncMessageItemDTO {

    /**
     * 数据Id
     */
    private Long bizId;
    /**
     * 数据类型
     */
    private Integer bizType;

    /**
     * 设置数据Id
     */
    public void setBizId(Long value) {
        this.bizId = value;
    }
    /**
     * 获取数据Id
     */
    public Long getBizId() {
        return this.bizId;
    }
    /**
     * 设置数据类型
     */
    public void setBizType(Integer value) {
        this.bizType = value;
    }
    /**
     * 获取数据类型
     */
    public Integer getBizType() {
        return this.bizType;
    }

}
