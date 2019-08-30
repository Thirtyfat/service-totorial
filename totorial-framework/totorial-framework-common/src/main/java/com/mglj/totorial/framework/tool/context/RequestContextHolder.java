package com.mglj.totorial.framework.tool.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zsp on 2018/8/30.
 */
public class RequestContextHolder {

    public static final String KEY_USER_CONTEXT = "userContext";

    public static final String REQUEST_HEADERS = "requestHeaders";

    private static ThreadLocal<Map<String, Object>> PARAM = new InheritableThreadLocal<>();

    public static void set(String key, Object value) {
        Map<String, Object> map = PARAM.get();
        if (map == null) {
            map = new HashMap<>();
            PARAM.set(map);
        }
        map.put(key, value);
    }

    public static <T> T get(String key) {
        Map<String, Object> map = PARAM.get();
        if (map == null) {
            return null;
        }
        return (T) map.get(key);
    }

    public static void remove(String key) {
        Map<String, Object> map = PARAM.get();
        if (map != null) {
            map.remove(key);
        }
    }

    public static void clear() {
        PARAM.remove();
    }

    public static void setUserContext(UserContext userContext) {
        set(KEY_USER_CONTEXT, userContext);
    }

    public static UserContext getUserContext() {
        return get(KEY_USER_CONTEXT);
    }

    public static void setRequestHeaders(Map<String, String> userContext) {
        set(REQUEST_HEADERS, userContext);
    }

    public static Map<String, String> getRequestHeaders() {
        return get(REQUEST_HEADERS);
    }




}
