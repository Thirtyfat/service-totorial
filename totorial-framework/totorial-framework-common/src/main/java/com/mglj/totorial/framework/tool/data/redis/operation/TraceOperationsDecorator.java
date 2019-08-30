package com.mglj.totorial.framework.tool.data.redis.operation;

import com.mglj.totorial.framework.tool.data.redis.RedisOperationEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Created by zsp on 2018/8/9.
 */
public class TraceOperationsDecorator extends AbstractOperations {

    private final static Logger logger = LoggerFactory.getLogger(TraceOperationsDecorator.class);

    public TraceOperationsDecorator(Operations operations) {
        super(operations);
        Objects.requireNonNull(operations, "The operations is null.");
    }

    @Override
    public void execute(RedisOperationEnum operation, String keyPrefix, Collection<String> keys,
                        Runnable command, Object... values) {
        long millis = System.currentTimeMillis();
        beginTrace(operation, keyPrefix, keys, millis, values);
        operations.execute(operation, keyPrefix, keys, command, values);
        endTrace(keyPrefix, millis, null);
    }

    @Override
    public <T> T execute(RedisOperationEnum operation, String keyPrefix, Collection<String> keys,
                         Callable<T> command, Object... values) {
        long millis = System.currentTimeMillis();
        beginTrace(operation, keyPrefix, keys, millis, values);
        T result = operations.execute(operation, keyPrefix, keys, command, values);
        endTrace(keyPrefix, millis, result);
        return result;
    }

    private void beginTrace(RedisOperationEnum operation,
                            String keyPrefix,
                            Collection<String> keys,
                            long millis,
                            Object... values) {

        if(logger.isInfoEnabled()) {
            StringBuilder builder = new StringBuilder();
            builder.append("redis> ")
                    .append(millis)
                    .append(" -domain ")
                    .append(keyPrefix)
                    .append(" -op ")
                    .append(operation);
            builder.append(" -key ");
            appendKeys(builder, keys);
            builder.append(" -val ");
            appendValues(builder, values);
            logger.info(builder.toString());
        }
    }

    private void endTrace(String keyPrefix, long millis, Object returnValue) {
        if(logger.isInfoEnabled()) {
            StringBuilder builder = new StringBuilder();
            builder.append("redis> ")
                    .append(millis)
                    .append(" -domain ")
                    .append(keyPrefix)
                    .append(" -cost ")
                    .append(System.currentTimeMillis() - millis);
            if(returnValue != null) {
                builder.append(" -ret ")
                        .append(returnValue);
            }
            logger.info(builder.toString());
        }
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
