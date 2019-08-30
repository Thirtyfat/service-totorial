package com.mglj.totorial.framework.mintor.aop;

import com.google.common.collect.Sets;
import com.mglj.totorial.framework.common.enums.DataSourceRoleEnum;
import com.mglj.totorial.framework.mintor.DataSourceException;
import com.mglj.totorial.framework.mintor.DataSourceRouteRule;
import com.mglj.totorial.framework.mintor.ShardingKeyHolder;
import com.mglj.totorial.framework.mintor.function.ShardingKeySetter;
import com.mglj.totorial.framework.mintor.util.FieldAccessUtils;
import io.shardingsphere.api.HintManager;
import io.shardingsphere.core.hint.HintManagerHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 根据DataSourceRouteRule注解设置数据源的分片、读写特性。
 * 一般先于Transactional的注解事务拦截前执行，这样Transactional的DataSource根据DataSourceRouteRule的拦截处理返回。
 *
 */
@Order(-1)
@Aspect
public class DataSourceRouteRuleAspect {

    private final Logger logger = LoggerFactory.getLogger(DataSourceRouteRuleAspect.class);

    private ShardingKeySetter shardingKeySetter;

    public DataSourceRouteRuleAspect(ShardingKeySetter shardingKeySetter) {
        this.shardingKeySetter = shardingKeySetter;
    }

    @Pointcut("@annotation(com.mglj.totorial.framework.mintor.DataSourceRouteRule)")
    public void access() {

    }

    @Around("access()")
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
        if(logger.isDebugEnabled()) {
            logger.debug("@DataSourceRouteRuleAspect");
        }

        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        DataSourceRouteRule dataSourceRouteRule = method.getAnnotation(DataSourceRouteRule.class);

        HintManager hintManager = null;
        try {
            if (Objects.equals(dataSourceRouteRule.role(), DataSourceRoleEnum.WRITABLE)) {
                HintManagerHolder.clear();
                hintManager = HintManager.getInstance();
                hintManager.setMasterRouteOnly();
            }
            if(dataSourceRouteRule.enableShardingKeyAssigned()) {
                ShardingKeyHolder.clear();
                setShardingKey(dataSourceRouteRule, method, pjp.getArgs());
            }
            return pjp.proceed();
        } finally {
            if(hintManager != null) {
                hintManager.close();
            }
            if(dataSourceRouteRule.enableShardingKeyAssigned()) {
                ShardingKeyHolder.clear();
            }
        }
    }

    private void setShardingKey(DataSourceRouteRule dataSourceRouteRule,
                                Method method,
                                Object[] arguments) {
        if(arguments == null || arguments.length == 0) {
            throw new DataSourceException("方法" + method.getName() + "没有定义入参，不支持分片键的设置");
        }
        String[] fields = dataSourceRouteRule.shardingKeyFields();
        Set<String> fieldSet;
        if(fields == null || fields.length == 0) {
            fieldSet = new HashSet<>();
        } else {
            fieldSet = Sets.newHashSet(fields);
        }
        String[] parameterNames = FieldAccessUtils.getParameterNames(method);
        if(fieldSet.isEmpty()) {
            //没有定义分片键字段，则留给具体的分片键设置实现来进行获取和设置
            shardingKeySetter.set(arguments, parameterNames);
        } else {
            //定义了分片键字段，则根据定义的字段获取键值映射表，然后交由具体的分片键设置实现来进行设置
            Map<String, Object> map = FieldAccessUtils.getAllFieldValue(arguments, parameterNames, fieldSet);
            if(!fieldSet.containsAll(map.keySet())) {
                throw new DataSourceException("未对所有的字段" + fieldSet + "赋值，不支持分片键的设置");
            }
            shardingKeySetter.set(map, parameterNames);
        }
    }

}
