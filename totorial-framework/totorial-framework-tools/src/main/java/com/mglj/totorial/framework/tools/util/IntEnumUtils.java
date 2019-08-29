package com.mglj.totorial.framework.tools.util;







import com.mglj.totorial.framework.tools.model.CodeEnum;

import java.util.Objects;

/**
 * Created by yj on 2018/7/13.
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
