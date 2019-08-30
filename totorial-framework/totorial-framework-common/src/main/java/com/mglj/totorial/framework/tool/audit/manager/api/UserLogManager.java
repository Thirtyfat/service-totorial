package com.mglj.totorial.framework.tool.audit.manager.api;


import com.mglj.totorial.framework.tool.audit.domain.UserLog;
import com.mglj.totorial.framework.tool.audit.domain.query.UserLogQuery;

import java.util.List;

/**
 * Created by zsp on 2018/8/31.
 */
public interface UserLogManager {

    void saveUserLog(UserLog userLog);

    List<UserLog> listUserLog(UserLogQuery query);

    int countUserLog(UserLogQuery query);

}
