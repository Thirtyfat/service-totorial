package com.mglj.totorial.framework.tool.audit.manager.impl;


import com.mglj.totorial.framework.tool.audit.domain.UserLog;
import com.mglj.totorial.framework.tool.audit.manager.api.UserLogCollectManager;
import com.mglj.totorial.framework.tool.mq.Producer;

/**
 * Created by zsp on 2018/9/4.
 */
public class UserLogCollectManagerImpl implements UserLogCollectManager {

    private Producer<UserLog> userLogProducer;
    public void setUserLogProducer(Producer<UserLog> userLogProducer) {
        this.userLogProducer = userLogProducer;
    }

    @Override
    public void collectUserLog(UserLog userLog) {
        userLogProducer.asyncSend(userLog);
    }

}
