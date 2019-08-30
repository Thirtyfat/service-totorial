package com.mglj.totorial.framework.mintor;

import com.google.common.collect.Sets;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;

/**
 * 数据源，连接池等属性的定义
 *
 * Created by zsp on 2019/3/8.
 */
public class PropertyNames {

    public final static String NAME_DRIVER_CLASS_NAME = "driverClassName";
    public final static String NAME_URL = "url";
    public final static String NAME_USERNAME = "username";
    public final static String NAME_PASSWORD = "password";
    public final static String NAME_NAME = "name";

    public final static String NAME_INITIAL_SIZE = "initialSize";
    public final static String NAME_MIN_IDLE = "minIdle";
    public final static String NAME_MAX_ACTIVE = "maxActive";
    public final static String NAME_MAX_WAIT = "maxWait";
    public final static String NAME_USE_UNFAIR_LOCK = "useUnfairLock";
    public final static String NAME_VALIDATION_QUERY = "validationQuery";
    public final static String NAME_TEST_ON_BORROW = "testOnBorrow";
    public final static String NAME_TEST_ON_RETURN = "testOnReturn";
    public final static String NAME_TEST_WHILE_IDLE = "testWhileIdle";
    public final static String NAME_TIME_BETWEEN_EVICTION_RUNS_MILLIS = "timeBetweenEvictionRunsMillis";
    public final static String NAME_MIN_EVICTABLE_IDLE_TIME_MILLIS = "minEvictableIdleTimeMillis";
    public final static String NAME_CONNECTION_INIT_SQLS = "connectionInitSqls";
    public final static String NAME_FILTERS = "filters";

    private static Set<String> NAMES;
    private static Map<String, Function<Object, String>> SERIALIZER;
    private static Map<String, Function<String, Object>> DESERIALIZER;

    private final static Function<Object, String> DEFAULT_SERIALIZER = e -> {
        Objects.requireNonNull(e);
        return String.valueOf(e);
    };

    private final static Function<String, Object> DEFAULT_DESERIALIZER = e -> {
        Objects.requireNonNull(e);
        return e;
    };

    static {
        NAMES = Sets.newHashSet(
                NAME_DRIVER_CLASS_NAME,
                NAME_URL,
                NAME_USERNAME,
                NAME_PASSWORD,
                NAME_NAME,

                NAME_INITIAL_SIZE,
                NAME_MIN_IDLE,
                NAME_MAX_ACTIVE,
                NAME_MAX_WAIT,
                NAME_USE_UNFAIR_LOCK,
                NAME_VALIDATION_QUERY,
                NAME_TEST_ON_BORROW,
                NAME_TEST_ON_RETURN,
                NAME_TEST_WHILE_IDLE,
                NAME_TIME_BETWEEN_EVICTION_RUNS_MILLIS,
                NAME_MIN_EVICTABLE_IDLE_TIME_MILLIS,
                NAME_CONNECTION_INIT_SQLS,
                NAME_FILTERS
        );

        Function<Object, String> StringListSerializer = e -> {
            Objects.requireNonNull(e);
            List<String> list = (List<String>)e;
            StringBuilder builder = new StringBuilder(256);
            for(int i = 0, len = list.size(), mask = len - 1; i < len; i++) {
                builder.append(list.get(i));
                if(i < mask) {
                    builder.append(",");
                }
            }
            return builder.toString();
        };
        SERIALIZER = new HashMap<>();
        SERIALIZER.put(NAME_CONNECTION_INIT_SQLS, StringListSerializer);

        Function<String, Object> IntDeserializer = e -> {
            Objects.requireNonNull(e);
            return Integer.parseInt(e);
        };
        Function<String, Object> LongDeserializer = e -> {
            Objects.requireNonNull(e);
            return Long.parseLong(e);
        };
        Function<String, Object> BooleanDeserializer = e -> {
            Objects.requireNonNull(e);
            return Boolean.parseBoolean(e);
        };
        Function<String, Object> StringListDeserializer = e -> {
            Objects.requireNonNull(e);
            List<String> list = new ArrayList<>();
            if(e != null) {
                String[] array = e.split(",");
                for(String item : array) {
                    list.add(item);
                }
            }
            return list;
        };
        DESERIALIZER = new HashMap<>();

        DESERIALIZER.put(NAME_INITIAL_SIZE, IntDeserializer);
        DESERIALIZER.put(NAME_MIN_IDLE, IntDeserializer);
        DESERIALIZER.put(NAME_MAX_ACTIVE, IntDeserializer);
        DESERIALIZER.put(NAME_MAX_WAIT, LongDeserializer);
        DESERIALIZER.put(NAME_USE_UNFAIR_LOCK, BooleanDeserializer);
        DESERIALIZER.put(NAME_TEST_ON_BORROW, BooleanDeserializer);
        DESERIALIZER.put(NAME_TEST_ON_RETURN, BooleanDeserializer);
        DESERIALIZER.put(NAME_TEST_WHILE_IDLE, BooleanDeserializer);
        DESERIALIZER.put(NAME_TIME_BETWEEN_EVICTION_RUNS_MILLIS, LongDeserializer);
        DESERIALIZER.put(NAME_MIN_EVICTABLE_IDLE_TIME_MILLIS, LongDeserializer);
        DESERIALIZER.put(NAME_CONNECTION_INIT_SQLS, StringListDeserializer);
    }

    /**
     * 校验属性的key是否合法
     *
     * @param keys
     * @return
     */
    public static boolean validate(Set<String> keys) {
        if(CollectionUtils.isEmpty(keys)) {
            return false;
        }
        return NAMES.containsAll(keys);
    }

    /**
     *
     * @param key
     * @return
     */
    public static Function<Object, String> getStringSerializer(String key) {
        Objects.requireNonNull(key);
        Function<Object, String> serializer = SERIALIZER.get(key);
        if(serializer != null) {
            return serializer;
        }
        return DEFAULT_SERIALIZER;
    }

    /**
     *
     * @param key
     * @return
     */
    public static Function<String, Object> getStringDeserializer(String key) {
        Objects.requireNonNull(key);
        Function<String, Object> deserializer = DESERIALIZER.get(key);
        if(deserializer != null) {
            return deserializer;
        }
        return DEFAULT_DESERIALIZER;
    }

    /**
     *
     * @return
     */
    public static String[] getKeyNames() {
        return NAMES.toArray(new String[0]);
    }

    public static void main(String[] args) {
        String s = getStringSerializer(NAME_CONNECTION_INIT_SQLS).apply(
                Arrays.asList("select * from dual;", "select 1;"));
        System.out.println(s);
        System.out.println(getStringDeserializer(NAME_CONNECTION_INIT_SQLS).apply(s));
        System.out.println(getStringDeserializer(NAME_INITIAL_SIZE).apply("10"));
        System.out.println(getStringDeserializer(NAME_TEST_ON_BORROW).apply("true"));
        String[] names = getKeyNames();
        System.out.println(StringUtils.arrayToDelimitedString(names, ","));
    }

}
