package com.mglj.totorial.framework.tool.security.jwt;


import com.mglj.totorial.framework.common.lang.Result;
import com.mglj.totorial.framework.tool.context.UserContext;

/**
 * token的解析工具
 *
 * Created by zsp on 2018/8/29.
 */
public interface TokenParser {

    /**
     * 验证token，包括验证token的时效性等
     *
     * @param token
     * @return
     */
    Result<UserContext> verify(String token);

    /**
     * 解析token，返回用户上下文信息
     *
     * @param token
     * @return
     */
    Result<UserContext> parse(String token);

}
