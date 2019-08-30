package com.mglj.totorial.framework.tool.dao.api;


import com.mglj.totorial.framework.tool.audit.domain.UserLog;
import com.mglj.totorial.framework.tool.audit.domain.po.UserLogPO;
import com.mglj.totorial.framework.tool.audit.domain.query.UserLogQuery;

import java.util.Collection;
import java.util.List;

/**
 * Created by zsp on 2018/8/30.
 */
public interface UserLogDao {

    int saveAllUserLog(Collection<UserLog> collection);

    List<UserLogPO> listUserLog(UserLogQuery query);

    int countUserLog(UserLogQuery query);

}
