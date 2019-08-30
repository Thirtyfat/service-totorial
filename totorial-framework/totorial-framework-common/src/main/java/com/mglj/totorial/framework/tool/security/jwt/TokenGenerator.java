package com.mglj.totorial.framework.tool.security.jwt;


import com.mglj.totorial.framework.tool.context.UserContext;

/**
 * token生成工具
 *
 * Created by zsp on 2018/8/29.
 */
public interface TokenGenerator {

    /**
     * 生成一个token
     *
     * @return
     */
    String generateToken();

    /**
     * 生成一个token
     *
     * @param expiredMillis     token过期时间（毫秒）
     * @return
     */
    String generateToken(long expiredMillis);

    /**
     * 生成一个token
     *
     * @param expiredMillis     token过期时间（毫秒）
     * @param delayMillis       token验证推迟时间（毫秒）
     * @return
     */
    String generateToken(long expiredMillis, long delayMillis);

    /**
     * 生成一个token
     *
     * @param userContext       用户上下文
     * @return
     */
    String generateToken(UserContext userContext);

    /**
     * 生成一个token
     *
     * @param expiredMillis     token过期时间（毫秒）
     * @param userContext       用户上下文
     * @return
     */
    String generateToken(long expiredMillis, UserContext userContext);

    /**
     * 生成一个token
     *
     * @param expiredMillis     token过期时间（毫秒）
     * @param delayMillis       token验证推迟时间（毫秒）
     * @param userContext       用户上下文
     * @return
     */
    String generateToken(long expiredMillis, long delayMillis, UserContext userContext);

}
