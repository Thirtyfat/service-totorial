package com.mglj.totorial.framework.tool.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.mglj.totorial.framework.common.util.UuidUtils;
import com.mglj.totorial.framework.tool.context.UserContext;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by zsp on 2018/8/29.
 */
public class TokenGeneratorImpl extends JwtBase implements TokenGenerator {

    @Override
    public String generateToken() {
        return generate(0,0, null);
    }

    @Override
    public String generateToken(long expiredMillis) {
        return generate(expiredMillis, 0, null);
    }

    @Override
    public String generateToken(long expiredMillis, long delayMillis) {
        return generate(expiredMillis, delayMillis, null);
    }

    @Override
    public String generateToken(UserContext userContext) {
        return generate(0, 0, userContext);
    }

    @Override
    public String generateToken(long expiredMillis, UserContext userContext) {
        return generate(expiredMillis, 0, userContext);
    }

    @Override
    public String generateToken(long expiredMillis, long delayMillis, UserContext userContext) {
        return generate(expiredMillis, delayMillis, userContext);
    }

    private String generate(long expiredMillis, long delayMillis, UserContext userContext) {
        Algorithm algorithm = getAlgorithm();
        JWTCreator.Builder builder = JWT.create()
                .withJWTId(UuidUtils.generate())
                .withIssuer(ISSUER);
        buildForTime(builder, expiredMillis, delayMillis);
        if(userContext != null) {
            buildForUserContext(builder, userContext);
        }
        buildForExtensions(builder);
        return builder.sign(algorithm);
    }

    protected void buildForTime(JWTCreator.Builder builder, long expiredMillis, long delayMillis) {
        Date issuedAt = new Date();
        Date notBefore;
        if(delayMillis > 0) {
            notBefore = new Date(issuedAt.getTime() + delayMillis);
        } else {
            notBefore = new Date(issuedAt.getTime() + JWT_DELAY_MILLIS);
        }
        Date expire;
        if(expiredMillis > 0) {
            expire = new Date(notBefore.getTime() + expiredMillis);
        } else {
            expire = new Date(notBefore.getTime() + JWT_EXPIRED_MILLIS);
        }
        builder.withIssuedAt(issuedAt)
                .withNotBefore(notBefore)
                .withExpiresAt(expire);
    }

    protected void buildForUserContext(JWTCreator.Builder builder, UserContext userContext) {
        builder.withClaim(USER_ID_KEY, userContext.getUserId())
                .withClaim(USER_NAME_KEY, userContext.getUserName());
        if(userContext.getOrganizationId() != null) {
            builder.withClaim(ORG_ID_KEY, userContext.getOrganizationId());
        }
        if(StringUtils.hasText(userContext.getOrganizationName())) {
            builder.withClaim(ORG_NAME_KEY, userContext.getOrganizationName());
        }
    }

    protected void buildForExtensions(JWTCreator.Builder builder) {
        //Do nothing, sub class should implement it.
    }

}
