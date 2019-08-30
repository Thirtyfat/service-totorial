package com.mglj.totorial.framework.tool.logging;

import org.slf4j.Logger;

/**
 * Created by zsp on 2018/9/4.
 */
public class LogUtils {


    public static void trace(Logger logger, String message) {
        if(logger.isTraceEnabled()) {
            logger.trace(message);
        }
    }

    public static void trace(Logger logger, String message, Object... args) {
        if(logger.isTraceEnabled()) {
            logger.trace(message, args);
        }
    }

    public static void trace(Logger logger, String message, Throwable t) {
        if(logger.isTraceEnabled()) {
            logger.trace(message, t);
        }
    }

    public static void debug(Logger logger, String message) {
        if(logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    public static void debug(Logger logger, String message, Object... args) {
        if(logger.isDebugEnabled()) {
            logger.debug(message, args);
        }
    }

    public static void debug(Logger logger, String message, Throwable t) {
        if(logger.isDebugEnabled()) {
            logger.debug(message, t);
        }
    }

    public static void info(Logger logger, String message) {
        if(logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    public static void info(Logger logger, String message, Object... args) {
        if(logger.isInfoEnabled()) {
            logger.info(message, args);
        }
    }

    public static void info(Logger logger, String message, Throwable t) {
        if(logger.isInfoEnabled()) {
            logger.info(message, t);
        }
    }

    public static void warn(Logger logger, String message) {
        if(logger.isWarnEnabled()) {
            logger.info(message);
        }
    }

    public static void warn(Logger logger, String message, Object... args) {
        if(logger.isWarnEnabled()) {
            logger.info(message, args);
        }
    }

    public static void warn(Logger logger, String message, Throwable t) {
        if(logger.isWarnEnabled()) {
            logger.info(message, t);
        }
    }

    public static void error(Logger logger, String message) {
        if(logger.isErrorEnabled()) {
            logger.error(message);
        }
    }

    public static void error(Logger logger, String message, Object... args) {
        if(logger.isErrorEnabled()) {
            logger.error(message, args);
        }
    }

    public static void error(Logger logger, String message, Throwable t) {
        if(logger.isErrorEnabled()) {
            logger.error(message, t);
        }
    }

    /**
     * 构建日志
     *
     * @param args
     * @return
     */
    public static String message(Object[] args) {
        return message(null, args, null);
    }

    /**
     * 构建日志
     *
     * @param message
     * @param args
     * @return
     */
    public static String message(String message, Object[] args) {
        return message(message, args, null);
    }

    /**
     * 构建日志
     *
     * @param args
     * @param result
     * @return
     */
    public static String message(Object[] args, Object result) {
        return message(null, args, result);
    }

    /**
     * 构建日志
     *
     * @param message
     * @param args
     * @param result
     * @return
     */
    public static String message(String message, Object[] args, Object result) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n----------log message begin----------\n");
        if(message != null) {
            builder.append("msg: ").append(message).append("\n");
        }
        if(args != null) {
            for(int i = 0, len = args.length; i < len; i++) {
                builder.append("arg").append(i).append(": ").append(args[i]).append("\n");
            }
        }
        if(result != null) {
            builder.append("result: ").append(result).append("\n");
        }
        builder.append("----------log message end----------\n");
        return builder.toString();
    }

}
