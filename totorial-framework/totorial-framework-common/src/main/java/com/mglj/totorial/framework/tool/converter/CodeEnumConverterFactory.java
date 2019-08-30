package com.mglj.totorial.framework.tool.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zsp on 2018/7/13.
 */
public class CodeEnumConverterFactory implements ConverterFactory<Integer, Enum> {

    private final Map<Class, Converter> converterMap = new HashMap<>();

    @Override
    public <T extends Enum> Converter<Integer, T> getConverter(Class<T> targetType) {
        Converter converter = converterMap.get(targetType);
        if(converter == null) {
            converter = new CodeEnumConverter(targetType);
            converterMap.put(targetType, converter);
        }
        return converter;
    }

}
