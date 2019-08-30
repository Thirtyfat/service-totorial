package com.mglj.totorial.framework.tool.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;

/**
 * Created by zsp on 2018/8/29.
 */
class JwtBase {

    // 用户ID的KEY
    protected static final String USER_ID_KEY = "uid";

    // 用户NAME的KEY
    protected static final String USER_NAME_KEY = "unm";

    // 部门组织ID的KEY
    protected static final String ORG_ID_KEY = "oid";

    // 部门组织NAME的KEY
    protected static final String ORG_NAME_KEY = "onm";

    // ISSUER
    protected static final String ISSUER = "yhdx";

    // 验证推迟时间（毫秒）
    protected static final long JWT_DELAY_MILLIS = 0L;

    // 过期时间（毫秒）: 24 * 6 * 60 * 60 * 1000
    protected static final long JWT_EXPIRED_MILLIS = 518400000L;

    // 安全KEY: "JH(*@f93h2yd7*GSWJ2"
    protected static final String JWT_SECRETKEY = "aAJmYF+B/9aNvlEe0L)ZYMblKsf6vMCNO<dfsafdsfg5435%GlD46+k+wjPifGgx|G29i9+.zBZU4uQ,1bkw";

    //加密算法
    protected Algorithm getAlgorithm() {
        return Algorithm.HMAC512(JWT_SECRETKEY);
    }

}
