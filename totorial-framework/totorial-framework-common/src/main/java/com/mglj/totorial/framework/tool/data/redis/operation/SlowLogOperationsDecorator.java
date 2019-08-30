package com.mglj.totorial.framework.tool.data.redis.operation;

import com.mglj.totorial.framework.tool.data.redis.RedisOperationEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * redis访问慢日志记录，访问消耗的时间包括：发起请求的网络时间+redis执行时间+返回响应的网络时间。
 *
 * Created by zsp on 2018/8/10.
 */
public class SlowLogOperationsDecorator extends AbstractOperations {

    private final static Logger logger = LoggerFactory.getLogger(SlowLogOperationsDecorator.class);

    private final static int ERROR_LOG_THRESHOLD = 5;
    private final static int COUNTER_RESET_SPAN = 3600_000;

    private long lastMillis = System.currentTimeMillis();
    private AtomicInteger counter = new AtomicInteger(0);

    private int logSlowRedisAccessThreshold;

    public int getLogSlowRedisAccessThreshold() {
        return logSlowRedisAccessThreshold;
    }

    public void setLogSlowRedisAccessThreshold(int value) {
        this.logSlowRedisAccessThreshold = value;
    }

    public SlowLogOperationsDecorator(Operations operations) {
        super(operations);
        Objects.requireNonNull(operations, "The operations is null.");
    }

    @Override
    public void execute(RedisOperationEnum operation, String keyPrefix, Collection<String> keys,
                        Runnable command, Object... values) {
        long millis = System.currentTimeMillis();
        operations.execute(operation, keyPrefix, keys, command, values);
        long cost = System.currentTimeMillis() - millis;
        if(cost > logSlowRedisAccessThreshold) {
            log(cost, operation, keyPrefix, keys, null, values);
        }
    }

    @Override
    public <T> T execute(RedisOperationEnum operation, String keyPrefix, Collection<String> keys,
                         Callable<T> command, Object... values) {
        long millis = System.currentTimeMillis();
        T returnValue = operations.execute(operation, keyPrefix, keys, command, values);
        long cost = System.currentTimeMillis() - millis;
        if(cost > logSlowRedisAccessThreshold) {
            log(cost, operation, keyPrefix, keys, returnValue, values);
        }
        return returnValue;
    }

    private void log(long cost, RedisOperationEnum operation, String keyPrefix,
                     Collection<String> keys, Object returnValue, Object... values) {
        int count = counter.incrementAndGet();
        long millis = System.currentTimeMillis();
        if((millis - lastMillis) > COUNTER_RESET_SPAN){
            synchronized (this) {
                if((millis - lastMillis) > COUNTER_RESET_SPAN) {
                    lastMillis = millis;
                    counter = new AtomicInteger(0);
                }
            }
        }
        if(count > ERROR_LOG_THRESHOLD) {
            logger.error(buildMessage(cost, operation, keyPrefix, keys, returnValue, values));
        } else {
            if(logger.isWarnEnabled()) {
                logger.warn(buildMessage(cost, operation, keyPrefix, keys, returnValue, values));
            }
        }
    }

    private String buildMessage(long cost, RedisOperationEnum operation, String keyPrefix,
                                Collection<String> keys, Object returnValue, Object... values) {
        StringBuilder builder = new StringBuilder();
        builder.append("redis slow> ")
                .append(" -domain ")
                .append(keyPrefix)
                .append(" -op ")
                .append(operation)
                .append(" -cost ")
                .append(cost);
        builder.append(" -key ");
        appendKeys(builder, keys);
        builder.append(" -val ");
        appendValues(builder, values);
        if(returnValue != null) {
            builder.append(" -ret ")
                    .append(returnValue);
        }
        return builder.toString();
    }

    private void appendKeys(StringBuilder builder, Collection<String> keys) {
        if(!CollectionUtils.isEmpty(keys)) {
            for(String key : keys) {
                builder.append(key).append(" ");
            }
        }
        builder.deleteCharAt(builder.length() - 1);
    }

    private void appendValues(StringBuilder builder, Object... values) {
        if(values != null && values.length > 0) {
            for(Object value : values) {
                builder.append(value).append(" ");
            }
        }
        builder.deleteCharAt(builder.length() - 1);
    }

}
