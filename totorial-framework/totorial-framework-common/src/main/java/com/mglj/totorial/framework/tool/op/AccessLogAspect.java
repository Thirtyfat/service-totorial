package com.mglj.totorial.framework.tool.op;

import com.mglj.totorial.framework.tool.context.RequestContextHolder;
import com.mglj.totorial.framework.tool.context.ServletContextHolder;
import com.mglj.totorial.framework.tool.context.UserContext;
import com.mglj.totorial.framework.tool.logging.LogUtils;
import com.mglj.totorial.framework.tool.util.CalendarUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zsp on 2018/11/21.
 */

@Order(1)
@Aspect
public class AccessLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(AccessLogAspect.class);

    @Pointcut("execution(* com.mglj..*.facade..*.*(..))")
    public void access() {

    }

    @Around("access()")
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
        if(!logger.isDebugEnabled()) {
            return pjp.proceed();
        }

        long start = System.currentTimeMillis();

        Object obj = pjp.proceed();

        Object bean = pjp.getTarget();
        Class<?> clazz = bean.getClass();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        Object[] arguments = pjp.getArgs();
        List<String> argumentList = new ArrayList<>();
        if(arguments != null && arguments.length > 0) {
            for(Object argument : arguments) {
                if(argument == null
                        || argument instanceof ServletRequest
                        || argument instanceof ServletResponse) {
                    continue;
                }
                argumentList.add(argument.toString());
            }
        }
        HttpServletRequest request = ServletContextHolder.getRequest();
        String uri =(request==null?"":request.getRequestURI());
        UserContext userContext = RequestContextHolder.getUserContext();
        long organizationId = 0L;
        long userId = 0L;
        String userName = "";
        if(userContext != null) {
            organizationId = userContext.getOrganizationId() == null ? 0L : userContext.getOrganizationId();
            userId = userContext.getUserId();
            userName = userContext.getUserName();
        }
        LogUtils.debug(logger,
                "url: {}, class: {}, method: {}, startAt: {}, cost: {}, organizationId: {}, userId: {}, userName: {}, data: {}",
                uri,
                clazz.getName(),
                method.getName(),
                CalendarUtils.format(new Date(start)),
                (System.currentTimeMillis() - start),
                organizationId,
                userId,
                userName,
                argumentList);

        return obj;
    }

}
