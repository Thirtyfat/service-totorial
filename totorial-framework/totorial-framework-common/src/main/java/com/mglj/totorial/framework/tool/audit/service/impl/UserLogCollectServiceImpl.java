package com.mglj.totorial.framework.tool.audit.service.impl;

import com.mglj.totorial.framework.tool.audit.domain.UserLog;
import com.mglj.totorial.framework.tool.audit.domain.builder.UserLogBuilder;
import com.mglj.totorial.framework.tool.audit.manager.api.UserLogCollectManager;
import com.mglj.totorial.framework.tool.audit.service.api.UserLogCollectService;
import com.mglj.totorial.framework.tool.context.ApplicationContext;
import com.mglj.totorial.framework.tool.context.RequestContextHolder;
import com.mglj.totorial.framework.tool.context.UserContext;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Date;

/**
 * Created by zsp on 2018/9/4.
 */
public class UserLogCollectServiceImpl implements UserLogCollectService {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private UserLogCollectManager userLogCollectManager;

    @Override
    public void collectUserLog(UserLogBuilder userLogBuilder) {
        UserContext userContext = RequestContextHolder.getUserContext();
        if(userContext != null) {
            userLogBuilder.set(userContext);
        }
        UserLog userLog = userLogBuilder.get();
        if(applicationContext != null) {
            userLog.setApplicationId(applicationContext.getApplicationId());
        } else {
            userLog.setApplicationId(0);
        }
        userLog.setOperationTime(new Date());
        userLogCollectManager.collectUserLog(userLog);
    }

}
