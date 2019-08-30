package com.mglj.totorial.framework.common.util;


import com.mglj.totorial.framework.common.lang.CodeEnum;

import java.util.Objects;

/**
 * Created by zsp on 2018/7/13.
 */
public class IntEnumUtils {

    public static <E extends CodeEnum> E getEnum(Class<E> targetType, int code){
        E[] enumConstants = targetType.getEnumConstants();
        for (E item : enumConstants) {
            if(Objects.equals(code, item.getCode())) {
                return item;
            }
        }
        return null;
    }

}
