package com.mglj.totorial.framework.tool.data.redis.locks;

import com.mglj.totorial.framework.tool.concurrent.locks.SimpleDisLock;
import com.mglj.totorial.framework.tool.concurrent.locks.SimpleDisLockFactory;
import com.mglj.totorial.framework.tool.concurrent.locks.SimpleDisLockToken;
import com.mglj.totorial.framework.tool.context.ApplicationContext;
import com.mglj.totorial.framework.tool.data.redis.StringOperationTemplate;
import com.mglj.totorial.framework.tool.data.redis.operation.Operations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zsp on 2018/9/19.
 */
public class RedisSimpleDisLockFactory implements SimpleDisLockFactory<String> {

    private Map<SimpleDisLockToken, SimpleDisLock<String>> registry = new HashMap<>();

    private ApplicationContext applicationContext;
    private StringRedisTemplate stringRedisTemplate;
    private Operations operations;

    public RedisSimpleDisLockFactory(ApplicationContext applicationContext,
                                     StringRedisTemplate stringRedisTemplate,
                                     Operations operations) {
        this.applicationContext = applicationContext;
        this.stringRedisTemplate = stringRedisTemplate;
        this.operations = operations;
    }

    @Override
    public SimpleDisLock<String> getSimpleDisLock(SimpleDisLockToken token) {
        RedisSimpleDisLockToken redisSimpleDisLockToken = (RedisSimpleDisLockToken)token;
        SimpleDisLock<String> simpleDisLock = registry.get(redisSimpleDisLockToken);
        if(simpleDisLock == null) {
            synchronized (this) {
                simpleDisLock = registry.get(redisSimpleDisLockToken);
                if(simpleDisLock == null) {
                    simpleDisLock = new RedisSimpleDisLock(new StringOperationTemplate(String.class,
                            String.class,
                            stringRedisTemplate,
                            redisSimpleDisLockToken.getNamespace(),
                            redisSimpleDisLockToken.getDomain(),
                            operations),
                            applicationContext.getDistributedId(),
                            16);
                    registry.put(redisSimpleDisLockToken, simpleDisLock);
                }
            }
        }
        return simpleDisLock;
    }

}
