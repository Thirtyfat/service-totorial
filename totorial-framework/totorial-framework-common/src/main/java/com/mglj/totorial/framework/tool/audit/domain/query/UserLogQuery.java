package com.mglj.totorial.framework.tool.audit.domain.query;


import com.mglj.totorial.framework.common.lang.PageQuery;

import java.util.Date;

/**
 * Created by zsp on 2018/9/19.
 */
public class UserLogQuery extends PageQuery {

    private Integer applicationId;
    private Long bizId;
    private String bizCode;
    private Long userId;
    private String userName;
    private String organizationName;
    private Date operationTimeStart;
    private Date operationTimeEnd;

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
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

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Date getOperationTimeStart() {
        return operationTimeStart;
    }

    public void setOperationTimeStart(Date operationTimeStart) {
        this.operationTimeStart = operationTimeStart;
    }

    public Date getOperationTimeEnd() {
        return operationTimeEnd;
    }

    public void setOperationTimeEnd(Date operationTimeEnd) {
        this.operationTimeEnd = operationTimeEnd;
    }
}
