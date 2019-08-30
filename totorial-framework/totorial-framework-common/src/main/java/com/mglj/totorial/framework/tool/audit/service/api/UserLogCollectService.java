package com.mglj.totorial.framework.tool.audit.service.api;


import com.mglj.totorial.framework.tool.audit.domain.builder.UserLogBuilder;

/**
 * Created by zsp on 2018/9/4.
 */
public interface UserLogCollectService {

    void collectUserLog(UserLogBuilder userLogBuilder);

}
