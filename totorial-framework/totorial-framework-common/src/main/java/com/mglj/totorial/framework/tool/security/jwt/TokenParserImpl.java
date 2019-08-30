package com.mglj.totorial.framework.tool.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mglj.totorial.framework.common.lang.Result;
import com.mglj.totorial.framework.tool.context.UserContext;

/**
 * Created by zsp on 2018/8/29.
 */
public class TokenParserImpl extends JwtBase implements TokenParser {

    @Override
    public Result<UserContext> verify(String token) {
        Algorithm algorithm = getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = verifier.verify(token);
        } catch (TokenExpiredException e) {
            return Result.errorWithMsg(Result.UNAUTHORIZED, "token已失效");
        } catch (JWTVerificationException e) {
            return Result.errorWithMsg(Result.UNAUTHORIZED, "token校验未通过");
        }

        return Result.result(getClaim(decodedJWT));
    }

    @Override
    public Result<UserContext> parse(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return Result.result(getClaim(decodedJWT));
    }

    protected UserContext getClaim(DecodedJWT decodedJWT) {
        UserContext userContext = new UserContext();
        userContext.setUserId(decodedJWT.getClaim(USER_ID_KEY).asLong());
        userContext.setUserName(decodedJWT.getClaim(USER_NAME_KEY).asString());
        Claim claim = decodedJWT.getClaim(ORG_ID_KEY);
        if(claim != null) {
            userContext.setOrganizationId(claim.asLong());
        }
        claim = decodedJWT.getClaim(ORG_NAME_KEY);
        if(claim != null) {
            userContext.setOrganizationName(claim.asString());
        }
        return userContext;
    }
}
