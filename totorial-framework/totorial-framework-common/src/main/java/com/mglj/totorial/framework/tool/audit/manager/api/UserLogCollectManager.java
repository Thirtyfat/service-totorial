package com.mglj.totorial.framework.tool.audit.manager.api;


import com.mglj.totorial.framework.tool.audit.domain.UserLog;

/**
 * Created by zsp on 2018/9/4.
 */
public interface UserLogCollectManager {

    void collectUserLog(UserLog userLog);

}
