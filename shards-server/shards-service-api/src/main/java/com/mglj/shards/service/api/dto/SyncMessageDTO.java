package com.mglj.shards.service.api.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by zsp on 2019/3/21.
 */
public class SyncMessageDTO {

    /**
     * 消息Id
     */
    private Long syncMessageId;
    /**
     * 数据操作
     */
    private Integer action;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 消息项
     */
    private List<SyncMessageItemDTO> itemList;

    /**
     * 确认项
     */
    private List<SyncMessageAckDTO> ackList;

    private String actionRemark;

    /**
     * 设置消息Id
     */
    public void setSyncMessageId(Long value) {
        this.syncMessageId = value;
    }
    /**
     * 获取消息Id
     */
    public Long getSyncMessageId() {
        return this.syncMessageId;
    }
    /**
     * 设置数据操作
     */
    public void setAction(Integer value) {
        this.action = value;
    }
    /**
     * 获取数据操作
     */
    public Integer getAction() {
        return this.action;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 设置创建时间
     */
    public void setCreateTime(Date value) {
        this.createTime = value;
    }
    /**
     * 获取创建时间
     */
    public Date getCreateTime() {
        return this.createTime;
    }

    public List<SyncMessageItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<SyncMessageItemDTO> itemList) {
        this.itemList = itemList;
    }

    public List<SyncMessageAckDTO> getAckList() {
        return ackList;
    }

    public void setAckList(List<SyncMessageAckDTO> ackList) {
        this.ackList = ackList;
    }

    public String getActionRemark() {
        return actionRemark;
    }

    public void setActionRemark(String actionRemark) {
        this.actionRemark = actionRemark;
    }
}
