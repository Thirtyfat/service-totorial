package com.mglj.totorial.framework.tool.audit.domain.builder;

import com.mglj.totorial.framework.tool.audit.domain.UserLog;
import com.mglj.totorial.framework.tool.context.UserContext;
import com.mglj.totorial.framework.tool.metadata.domain.Operation;
import org.springframework.util.StringUtils;

/**
 * Created by zsp on 2018/8/30.
 */
public class UserLogBuilder {

    private UserLog userLog;
    private StringBuilder description;

    public UserLogBuilder() {
        this.userLog = new UserLog();
        this.description = new StringBuilder();
    }

    public UserLog get() {
        this.userLog.setDescription(description.toString());
        return this.userLog;
    }

    public UserLogBuilder set(UserContext userContext) {
        this.userLog.setUserId(userContext.getUserId());
        this.userLog.setUserName(userContext.getUserName());
        Long organizationId = userContext.getOrganizationId();
        if(organizationId != null) {
            this.userLog.setOrganizationId(organizationId);
        } else {
            this.userLog.setOrganizationId(0L);
        }
        this.userLog.setOrganizationName(userContext.getOrganizationName());
        return this;
    }

    public UserLogBuilder setOperation(Operation operation) {
        this.userLog.setOperationHashCode(operation.getHashCode());
        this.userLog.setOperationOffset(operation.getOffset());
        return this;
    }

    public UserLogBuilder setBizId(Long bizId) {
        this.userLog.setBizId(bizId);
        return this;
    }

    public UserLogBuilder setBizCode(String bizCode) {
        this.userLog.setBizCode(bizCode);
        return this;
    }

    public UserLogBuilder appendDescription(String str) {
        if(StringUtils.hasText(str)) {
            this.description.append(str);
        }
        return this;
    }

    public UserLogBuilder setCost(long cost) {
        this.userLog.setCost(cost);
        return this;
    }

}
