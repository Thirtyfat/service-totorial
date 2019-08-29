package com.mglj.totorial.framework.tools.concurrent;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;


public class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {

    private final String threadName;
    private final Consumer<String> logger;

    public AbortPolicyWithReport(String threadName) {
        this.threadName = threadName;
        this.logger = System.out::println;
    }

    public AbortPolicyWithReport(String threadName,
                                 Consumer<String> logger) {
        this.threadName = threadName;
        this.logger = logger;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        String msg = String.format("Thread pool is EXHAUSTED!" +
                        " Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d)," +
                        " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)!" ,
                threadName, e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(), e.getMaximumPoolSize(), e.getLargestPoolSize(),
                e.getTaskCount(), e.getCompletedTaskCount(), e.isShutdown(), e.isTerminated(), e.isTerminating());
        if(logger != null) {
            logger.accept(msg);
        }
        throw new RejectedExecutionException(msg);
    }


}
