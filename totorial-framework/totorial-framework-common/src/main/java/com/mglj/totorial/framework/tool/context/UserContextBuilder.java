package com.mglj.totorial.framework.tool.context;

/**
 * Created by zsp on 2018/8/29.
 */
public class UserContextBuilder {

    private UserContext userContext;

    public UserContextBuilder() {
        this.userContext = new UserContext();
    }

    public UserContextBuilder setUserId(Long userId){
        userContext.setUserId(userId);
        return this;
    }

    public UserContextBuilder setUserName(String userName){
        userContext.setUserName(userName);
        return this;
    }

    public UserContextBuilder setOrganizationId(Long organizationId){
        userContext.setOrganizationId(organizationId);
        return this;
    }

    public UserContextBuilder setOrganizationName(String organizationName){
        userContext.setOrganizationName(organizationName);
        return this;
    }

    public UserContext get() {
        return this.userContext;
    }

}
