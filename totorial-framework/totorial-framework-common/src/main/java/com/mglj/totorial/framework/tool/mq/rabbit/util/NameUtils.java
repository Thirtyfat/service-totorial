package com.mglj.totorial.framework.tool.mq.rabbit.util;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by zsp on 2018/8/31.
 */
public class NameUtils {

    /**
     * 死信名称后缀名
     */
    public final static String NAME_DEAD_LETTER_POSTFIX = "dlx";
    /**
     * rabbitmq键的组合连字符
     */
    public final static String NAME_HYPHEN = ".";

    /**
     * 死信队列
     */
    public final static String KEY_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
    /**
     * 死信队列
     */
    public final static String KEY_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
    /**
     * 消息条数限制
     */
    public final static String KEY_MAX_LENGTH = "x-max-length";
    /**
     * 消息容量限制
     */
    public final static String KEY_MAX_LENGTH_BYTES ="x-max-length-bytes";
    /**
     * 消息的生存周期，生存时间到了，消息会被从队里中删除
     */
    public final static String KEY_MESSAGE_TTL_ = "x-message-ttl";
    /**
     * 消息在指定的时间没有被访问(consume, basicGet, queueDeclare…)就会被删除
     */
    public final static String KEY_EXPIRES = "x-expires";

    /**
     *
     * @param name
     * @return
     */
    public static String wrapDeadLetterName(String name) {
        Objects.requireNonNull(name);
        return StringUtils.collectionToDelimitedString(
                Arrays.asList(name, NAME_DEAD_LETTER_POSTFIX), NAME_HYPHEN);
    }

}
