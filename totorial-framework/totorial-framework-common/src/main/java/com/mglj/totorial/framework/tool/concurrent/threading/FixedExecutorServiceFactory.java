package com.mglj.totorial.framework.tool.concurrent.threading;

import com.mglj.totorial.framework.common.concurrent.AbortPolicyWithReport;
import com.mglj.totorial.framework.common.concurrent.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zsp on 2018/9/4.
 */
public class FixedExecutorServiceFactory extends AbstractExecutorServiceFactory {

    private static final Logger logger = LoggerFactory.getLogger(FixedExecutorServiceFactory.class);

    private final static int DEFAULT_POOL_SIZE =  Runtime.getRuntime().availableProcessors() - 1;
    private final static int DEFAULT_QUEUE_SIZE = 1024;

    private final int poolSize;
    private final int queueSize;

    public FixedExecutorServiceFactory(String name) {
        this(name, false, DEFAULT_POOL_SIZE, DEFAULT_QUEUE_SIZE);
    }

    public FixedExecutorServiceFactory(String name, boolean daemo) {
        this(name, daemo, DEFAULT_POOL_SIZE, DEFAULT_QUEUE_SIZE);
    }

    public FixedExecutorServiceFactory(String name, int poolSize, int queueSize) {
        this(name, false, poolSize, queueSize);
    }

    public FixedExecutorServiceFactory(String name, boolean daemo, int poolSize, int queueSize) {
        super(name, daemo);
        if(poolSize < 1) {
            throw new IllegalArgumentException("The poolSize should be lager than 0.");
        }
        this.poolSize = poolSize;
        if(queueSize < 1) {
            throw new IllegalArgumentException("The queueSize should be lager than 0.");
        }
        this.queueSize = queueSize;
    }

    @Override
    public ExecutorService get() {
        if(threadPool == null) {
            synchronized (this) {
                if(threadPool == null) {
                    threadPool = new ThreadPoolExecutor(poolSize, poolSize,
                            0, TimeUnit.MILLISECONDS,
                            new LinkedBlockingQueue<>(queueSize),
                            new NamedThreadFactory(name, daemo),
                            new AbortPolicyWithReport(name, logger::warn));
                }
            }
        }

        return threadPool;
    }

}
