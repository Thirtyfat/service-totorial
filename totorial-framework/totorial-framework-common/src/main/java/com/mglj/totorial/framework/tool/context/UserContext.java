package com.mglj.totorial.framework.tool.context;

/**
 * 用户上下文
 *
 * Created by zsp on 2018/8/29.
 */
public class UserContext {

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 组织ID（例如仓库、服务站等单位部门）
     */
    private Long organizationId;
    /**
     * 组织名称（例如仓库、服务站等单位部门）
     */
    private String organizationName;

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
}
