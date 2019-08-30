package com.mglj.totorial.framework.tool.audit;

import com.mglj.totorial.framework.tool.audit.domain.builder.UserLogBuilder;
import com.mglj.totorial.framework.tool.context.UserContext;
import org.springframework.util.StringUtils;

/**
 * Created by zsp on 2018/8/30.
 */
public class UserLogContextHolder {

    private final static ThreadLocal<UserLogBuilder> log = new InheritableThreadLocal<>();

    public static void collect(String description) {
        if(StringUtils.hasText(description)) {
            getLog().appendDescription(description);
        }
    }

    public static void collect(Long bizId, String bizCode) {
        getLog().setBizId(bizId).setBizCode(bizCode);
    }

    public static void collect(Long bizId, String bizCode, String description) {
        collect(bizId, bizCode);
        collect(description);
    }

    public static void collect(UserContext userContext) {
        getLog().set(userContext);
    }

    public static UserLogBuilder get() {
        return getLog();
    }

    public static void clear() {
        log.remove();
    }

    private static UserLogBuilder getLog() {
        UserLogBuilder userLogBuilder = log.get();
        if(userLogBuilder == null) {
            userLogBuilder = new UserLogBuilder();
            log.set(userLogBuilder);
        }
        return userLogBuilder;
    }

}
