package com.mglj.totorial.framework.tool.converter;

import com.mglj.totorial.framework.common.lang.CodeEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zsp on 2018/7/13.
 */
public class CodeEnumConverter<T extends Enum<T>> implements Converter<Integer, T> {

    private final Class<T> targetType;
    private Map<Integer, T> cache = new HashMap<>();

    public CodeEnumConverter(Class<T> targetType) {
        this.targetType = targetType;
        T[] enumConstants = targetType.getEnumConstants();
        for(T item : enumConstants) {
            cache.put(((CodeEnum)item).getCode(), item);
        }
    }

    @Nullable
    @Override
    public T convert(Integer source) {
        T result = cache.get(source);
        if(result == null) {
            throw new IllegalArgumentException("No element matches " + source);
        }
        return result;
    }

}
