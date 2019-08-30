package com.mglj.totorial.framework.tool.audit.service.impl;

import com.mglj.totorial.framework.tool.audit.domain.UserLog;
import com.mglj.totorial.framework.tool.audit.domain.query.UserLogQuery;
import com.mglj.totorial.framework.tool.audit.manager.api.UserLogManager;
import com.mglj.totorial.framework.tool.audit.service.api.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by zsp on 2018/8/31.
 */
public class UserLogServiceImpl implements UserLogService {

    @Autowired
    private UserLogManager userLogManager;

    @Override
    public void saveUserLog(UserLog userLog) {
        userLogManager.saveUserLog(userLog);
    }

    @Override
    public List<UserLog> listUserLog(UserLogQuery query) {
        return userLogManager.listUserLog(query);
    }

    @Override
    public int countUserLog(UserLogQuery query) {
        return userLogManager.countUserLog(query);
    }

}
