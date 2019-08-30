package com.mglj.totorial.framework.tool.audit.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户操作日志
 *
 * Created by zsp on 2018/8/30.
 */
public class UserLog implements Serializable {

    private static final long serialVersionUID = -3478496237061558845L;

    /**
     * ID
     */
    private Long id;
    /**
     * 应用名称
     */
    private Integer applicationId;
    /**
     * 操作hashCode
     */
    private Integer operationHashCode;
    /**
     * 操作hash偏移
     */
    private Integer operationOffset;
    /**
     * 业务ID
     */
    private Long bizId;
    /**
     * 业务编码
     */
    private String bizCode;
    /**
     * 描述
     */
    private String description;
    /**
     * 操作人ID
     */
    private Long userId;
    /**
     * 操作人姓名
     */
    private String userName;
    /**
     * 单位组织ID，例如仓库
     */
    private Long organizationId;
    /**
     * 单位组织名称
     */
    private String organizationName;
    /**
     * 操作时间
     */
    private Date operationTime;
    /**
     * 花费时间（毫秒）
     */
    private Long cost;
    /**
     * 创建时间
     */
    private Date createTime;

    private String title;

    private String group;

    @Override
    public String toString() {
        return "UserLog{" +
                "id=" + id +
                ", applicationId='" + applicationId + '\'' +
                ", operationHashCode=" + operationHashCode +
                ", operationOffset=" + operationOffset +
                ", bizId=" + bizId +
                ", bizCode='" + bizCode + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", organizationId=" + organizationId +
                ", organizationName='" + organizationName + '\'' +
                ", operationTime=" + operationTime +
                ", cost=" + cost +
                ", createTime=" + createTime +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public Integer getOperationHashCode() {
        return operationHashCode;
    }

    public void setOperationHashCode(Integer operationHashCode) {
        this.operationHashCode = operationHashCode;
    }

    public Integer getOperationOffset() {
        return operationOffset;
    }

    public void setOperationOffset(Integer operationOffset) {
        this.operationOffset = operationOffset;
    }

    public Long getBizId() {
        return bizId;
    }

    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
