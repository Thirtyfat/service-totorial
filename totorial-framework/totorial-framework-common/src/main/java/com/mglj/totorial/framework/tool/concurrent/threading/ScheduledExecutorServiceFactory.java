package com.mglj.totorial.framework.tool.concurrent.threading;

import com.mglj.totorial.framework.common.concurrent.AbortPolicyWithReport;
import com.mglj.totorial.framework.common.concurrent.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by zsp on 2018/11/10.
 */
public class ScheduledExecutorServiceFactory extends AbstractExecutorServiceFactory {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledExecutorServiceFactory.class);

    private final static int DEFAULT_POOL_SIZE =  1;

    private final int poolSize;

    public ScheduledExecutorServiceFactory(String name) {
        this(name, false , DEFAULT_POOL_SIZE);
    }

    public ScheduledExecutorServiceFactory(String name, boolean daemo) {
        this(name, daemo, DEFAULT_POOL_SIZE);
    }

    public ScheduledExecutorServiceFactory(String name, int poolSize) {
        this(name, false, poolSize);
    }

    public ScheduledExecutorServiceFactory(String name, boolean daemo, int poolSize) {
        super(name, daemo);
        if(poolSize < 1) {
            throw new IllegalArgumentException("The poolSize should be lager than 0.");
        }
        this.poolSize = poolSize;
    }

    @Override
    public ExecutorService get() {
        if(threadPool == null) {
            synchronized (this) {
                if(threadPool == null) {
                    threadPool = new ScheduledThreadPoolExecutor(poolSize,
                            new NamedThreadFactory(name, daemo),
                            new AbortPolicyWithReport(name, logger::warn));
                }
            }
        }

        return threadPool;
    }
}
