package com.mglj.totorial.framework.tool.concurrent.threading;

import com.mglj.totorial.framework.tool.logging.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zsp on 2018/9/4.
 */
public abstract class AbstractExecutorServiceFactory implements ExecutorServiceFactory {

    private static final Logger logger = LoggerFactory.getLogger(AbstractExecutorServiceFactory.class);

    protected final String name;
    protected final boolean daemo;
    protected volatile ExecutorService threadPool;

    public AbstractExecutorServiceFactory(String name) {
        this(name, false);
    }

    public AbstractExecutorServiceFactory(String name, boolean daemo) {
        Objects.requireNonNull(name, "The name is null.");
        this.name = name;
        this.daemo = daemo;
    }

    @Override
    public void destroy() {
        if(threadPool == null) {
            return;
        }
        threadPool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                    LogUtils.error(logger, "Thread pool did not terminate.");
                }
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            threadPool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
