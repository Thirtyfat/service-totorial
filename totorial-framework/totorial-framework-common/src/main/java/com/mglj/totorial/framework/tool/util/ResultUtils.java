package com.mglj.totorial.framework.tool.util;

import com.mglj.totorial.framework.common.exceptions.BadRequestException;
import com.mglj.totorial.framework.common.exceptions.ObjectNotFoundException;
import com.mglj.totorial.framework.common.exceptions.ServiceUnavailableException;
import com.mglj.totorial.framework.common.lang.Result;
import com.mglj.totorial.framework.tool.logging.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Result的处理工具，简化Result的处理逻辑
 *
 * Created by zsp on 2018/9/26.
 */
public class ResultUtils {

    private final static Logger logger = LoggerFactory.getLogger(ResultUtils.class);
    private final static int DEFAULT_TRY_COUNT = 3;

    /**
     * 处理无返回值的Result结果；用true表示Result.OK的情况，其它用false表示。
     *
     * @param result
     * @return
     */
    public static boolean execute(Result<?> result) {
        return execute(result, (e) -> {
            LogUtils.warn(logger, e.getMsg() + "(" + e.getCode() + ")");
        });
    }

    /**
     * 处理无返回值的Result结果；用true表示Result.OK的情况，其它用false表示。
     *
     * @param result
     * @param errorHandler      非Result.OK情况下，自定义的处理操作
     * @return
     */
    public static boolean execute(Result<?> result, Consumer<Result<?>> errorHandler) {
        Objects.requireNonNull(result);
        if(result.wasOk()) {
            return true;
        }
        if(errorHandler != null) {
            errorHandler.accept(result);
        }
        return false;
    }

    /**
     * 处理无返回值的Result结果；用true表示Result.OK的情况，其它用false表示。
     *
     * @param delegate       返回Result结果的代理方法
     * @return
     */
    public static boolean execute(Supplier<Result<?>> delegate) {
        Objects.requireNonNull(delegate);
        try {
            Result<?> result = delegate.get();
            return execute(result);
        } catch (Exception e) {
            throw new ServiceUnavailableException(e);
        }
    }

    /**
     * 处理无返回值的Result结果；用true表示Result.OK的情况，其它用false表示。
     *
     * @param delegate       返回Result结果的代理方法
     * @param errorHandler  非Result.OK情况下，自定义的处理操作
     * @return
     */
    public static boolean execute(Supplier<Result<?>> delegate, Consumer<Result<?>> errorHandler) {
        Objects.requireNonNull(delegate);
        try {
            Result<?> result = delegate.get();
            return execute(result, errorHandler);
        } catch (Exception e) {
            throw new ServiceUnavailableException(e);
        }
    }

    /**
     * 处理有返回值的Result结果。
     *
     * @param result
     * @param <T>
     * @return
     */
    public static <T> T get(Result<T> result) {
        return get(result, (e) -> {
            LogUtils.warn(logger, e.getMsg() + "(" + e.getCode() + ")");
        });
    }

    /**
     * 处理有返回值的Result结果。
     *
     * @param result
     * @param errorHandler      非Result.OK情况下，自定义的处理操作
     * @param <T>
     * @return
     */
    public static <T> T get(Result<T> result, Consumer<Result<?>> errorHandler) {
        Objects.requireNonNull(result);
        if(result.wasOk()) {
            return result.getResult();
        }
        if(errorHandler != null) {
            errorHandler.accept(result);
        }
        return null;
    }

    /**
     * 处理有返回值的Result结果。
     *
     * @param delegate      返回Result结果的代理方法
     * @param <T>
     * @return
     */
    public static <T> T get(Supplier<Result<T>> delegate) {
        Objects.requireNonNull(delegate);
        try {
            Result<T> result = delegate.get();
            return get(result);
        } catch (Exception e) {
            throw new ServiceUnavailableException(e);
        }
    }

    /**
     * 处理有返回值的Result结果。
     *
     * @param delegate      返回Result结果的代理方法
     * @param errorHandler  非Result.OK情况下，自定义的处理操作
     * @param <T>
     * @return
     */
    public static <T> T get(Supplier<Result<T>> delegate, Consumer<Result<?>> errorHandler) {
        Objects.requireNonNull(delegate);
        try {
            Result<T> result = delegate.get();
            return get(result, errorHandler);
        } catch (Exception e) {
            throw new ServiceUnavailableException(e);
        }
    }

    /**
     * 处理有返回值的Result结果；若返回失败或结果为空，则进行多次尝试。
     *
     * @param delegate          返回Result结果的代理方法
     * @param <T>
     * @return
     */
    public static <T> T tryGet(Supplier<Result<T>> delegate) {
        return tryGet(DEFAULT_TRY_COUNT, delegate, null);
    }

    /**
     * 处理有返回值的Result结果；若返回失败或结果为空，则进行多次尝试。
     *
     * @param delegate          返回Result结果的代理方法
     * @param messageBuilder    若返回失败，构建异常信息的代理方法
     * @param <T>
     * @return
     */
    public static <T> T tryGet(Supplier<Result<T>> delegate,
                               Function<String, String> messageBuilder) {
        return tryGet(DEFAULT_TRY_COUNT, delegate, messageBuilder);
    }

    /**
     * 处理有返回值的Result结果；若返回失败或结果为空，则进行多次尝试。
     *
     * @param tryCount          尝试次数
     * @param delegate          返回Result结果的代理方法
     * @param <T>
     * @return
     */
    public static <T> T tryGet(int tryCount,
                               Supplier<Result<T>> delegate) {
        return tryGet(tryCount, delegate, null);
    }

    /**
     * 处理有返回值的Result结果；若返回失败或结果为空，则进行多次尝试。
     *
     * @param tryCount          尝试次数
     * @param delegate          返回Result结果的代理方法
     * @param messageBuilder    若返回失败，构建异常信息的代理方法
     * @param <T>
     * @return
     */
    public static <T> T tryGet(int tryCount,
                               Supplier<Result<T>> delegate,
                               Function<String, String> messageBuilder) {
        if(tryCount < 1) {
            throw new IllegalArgumentException("tryCount");
        }
        if(delegate == null) {
            throw new IllegalArgumentException("delegate");
        }
        T obj;
        Result<T> result;
        String message = null;
        Exception ex = null;
        for(int i = 0; i < tryCount; i++) {
            try {
                result = delegate.get();
                if (result.wasOk() && (obj = result.getResult()) != null) {
                    return obj;
                }
                message = result.getMsg() + "(" + result.getCode() + ")";
            } catch (Exception e) {
                message = e.getMessage();
                ex = e;
            }
        }
        throw new ServiceUnavailableException(messageBuilder == null ? message : messageBuilder.apply(message), ex);
    }

    public static <T> T getOrException(Result<T> result) {
        return getOrException(result, null);
    }

    public static <T> T getOrException(Supplier<Result<T>> delegate) {
        Objects.requireNonNull(delegate);
        return getOrException(delegate.get(), null);
    }

    public static <T> T getOrException(Result<T> result, String message) {
        Objects.requireNonNull(result);
        if(result.wasOk()) {
            return result.getResult();
        }
        if(result.wasNotFound()) {
            throw new ObjectNotFoundException(message);
        }
        if(result.wasBadRequest()) {
            throw new BadRequestException(message);
        }
        throw new ServiceUnavailableException(message);
    }

    public static <T> T getOrException(Supplier<Result<T>> delegate, String message) {
        Objects.requireNonNull(delegate);
        return getOrException(delegate.get(), message);
    }

    public static <T> ResultWrapper<T> tryTo(Supplier<Result<T>> delegate) {
        return new ResultWrapper(delegate);
    }

    public static <T> ResultWrapper<T> tryTo(Supplier<Result<T>> delegate, int count) {
        ResultWrapper wrapper = new ResultWrapper(delegate);
        wrapper.setCount(count);
        return wrapper;
    }

    public static <T> ResultWrapper<T> tryTo(Supplier<Result<T>> delegate, long intervalMillis) {
        ResultWrapper wrapper = new ResultWrapper(delegate);
        wrapper.setIntervalMillis(intervalMillis);
        return wrapper;
    }

    public static <T> ResultWrapper<T> tryTo(Supplier<Result<T>> delegate, int count, long intervalMillis) {
        ResultWrapper wrapper = new ResultWrapper(delegate);
        wrapper.setCount(count);
        wrapper.setIntervalMillis(intervalMillis);
        return wrapper;
    }

    public static <T> ResultWrapper<T> tryTo(Supplier<Result<T>> delegate, String message) {
        ResultWrapper wrapper = new ResultWrapper(delegate);
        wrapper.setMessage(message);
        return wrapper;
    }

    public static <T> ResultWrapper<T> tryTo(Supplier<Result<T>> delegate, String message, int count) {
        ResultWrapper wrapper = new ResultWrapper(delegate);
        wrapper.setMessage(message);
        wrapper.setCount(count);
        return wrapper;
    }

    public static <T> ResultWrapper<T> tryTo(Supplier<Result<T>> delegate, String message, long intervalMillis) {
        ResultWrapper wrapper = new ResultWrapper(delegate);
        wrapper.setMessage(message);
        wrapper.setIntervalMillis(intervalMillis);
        return wrapper;
    }

    public static <T> ResultWrapper<T> tryTo(Supplier<Result<T>> delegate, String message, int count, long intervalMillis) {
        ResultWrapper wrapper = new ResultWrapper(delegate);
        wrapper.setMessage(message);
        wrapper.setCount(count);
        wrapper.setIntervalMillis(intervalMillis);
        return wrapper;
    }

    public static class ResultWrapper<T> {

        private Supplier<Result<T>> delegate;
        private int count = DEFAULT_TRY_COUNT;
        private long intervalMillis;
        private String message;

        public ResultWrapper(Supplier<Result<T>> delegate) {
            Objects.requireNonNull(delegate);
            this.delegate = delegate;
        }

        public void setCount(int count) {
            if(count < 1) {
                throw new IllegalArgumentException();
            }
            this.count = count;
        }

        public void setIntervalMillis(long intervalMillis) {
            if(intervalMillis < 0) {
                throw new IllegalArgumentException();
            }
            this.intervalMillis = intervalMillis;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T get() {
            return tryToGet(false);
        }

        public T getOrException() {
            T value = tryToGet(true);
            if(value == null) {
                throw new ServiceUnavailableException();
            }
            return value;
        }

        public T getOrException(String message) {
            T value = tryToGet(true);
            if(value == null) {
                throw new ServiceUnavailableException(message);
            }
            return value;
        }

        private T tryToGet(boolean throwable) {
            int maxCount = count;
            int loop = 0;
            Result<T> result;
            T value = null;
            for(;;) {
                if(loop >= maxCount) {
                    break;
                }
                if(intervalMillis > 0) {
                    try {
                        Thread.sleep(intervalMillis);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                try {
                    result = delegate.get();
                } catch (Exception e) {
                    LogUtils.error(logger, e.getMessage(), e);
                    continue;
                } finally {
                    loop++;
                }
                if(result == null) {
                    throw new ServiceUnavailableException("The result of delegate is null.");
                }
                if (result.wasOk()) {
                    value = result.getResult();
                    break;
                }
                if(throwable) {
                    if (result.wasNotFound()) {
                        throw new ObjectNotFoundException(message);
                    }
                    if (result.wasBadRequest()) {
                        throw new BadRequestException(message);
                    }
                } else {
                    if(result.wasNotFound() || result.wasBadRequest()) {
                        break;
                    }
                }
            }
            return value;
        }
    }

}
