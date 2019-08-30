package com.mglj.totorial.framework.tool.data.redis.operation;

import com.mglj.totorial.framework.tool.data.redis.RedisOperationEnum;
import com.mglj.totorial.framework.tool.data.redis.RedisOperationException;
import com.mglj.totorial.framework.tool.logging.LogBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by zsp on 2018/8/9.
 */
public class GenericOperations extends AbstractOperations {

    private final static Logger logger = LoggerFactory.getLogger(GenericOperations.class);

    public GenericOperations() {
        super(null);
    }

    @Override
    public void execute(RedisOperationEnum operation, String keyPrefix, Collection<String> keys,
                        Runnable command, Object... values) {
        try {
            command.run();
        } catch (Exception e) {
            logger.error(LogBuilder.create().from(getErrorMsg(operation, keyPrefix), merge(keys, values)), e);
            throw new RedisOperationException(e);
        }
    }

    @Override
    public <T> T execute(RedisOperationEnum operation, String keyPrefix, Collection<String> keys,
                         Callable<T> command, Object... values) {
        try {
            return command.call();
        } catch (Exception e) {
            logger.error(LogBuilder.create().from(getErrorMsg(operation, keyPrefix), merge(keys, values)), e);
            throw new RedisOperationException(e);
        }
    }

    private String getErrorMsg(RedisOperationEnum operation, String keyPrefix) {
        return "访问redis失败，领域[" + keyPrefix + "]，方法[" + operation + "]：";
    }

    private List<Object> merge(Collection<String> keys, Object... args) {
        List<Object> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(keys)) {
            for(String key : keys) {
                list.add(key);
            }
        }
        if(args!= null && args.length > 0) {
            for(Object arg : args) {
                list.add(arg);
            }
        }
        return list;
    }

}
