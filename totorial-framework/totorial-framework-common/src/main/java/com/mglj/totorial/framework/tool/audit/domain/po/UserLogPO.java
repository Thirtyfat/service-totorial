package com.mglj.totorial.framework.tool.audit.domain.po;

import java.util.Date;

/**
 * Created by jcx on 2018/9/29.
 */
public class UserLogPO {
    private Long id;
    private Integer application;
    private Integer operationHashCode;
    private Integer operationOffset;
    private Long bizId;
    private String bizCode;
    private String description;
    private Long userId;
    private String userName;
    private Long organizationId;
    private String organizationName;
    private Date operationTime;
    private Long cost;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getApplication() {
        return application;
    }

    public void setApplication(Integer application) {
        this.application = application;
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

}
