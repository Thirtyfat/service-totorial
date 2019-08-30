package com.mglj.totorial.framework.common.lang;

/**
 *
 * @param <E>   The enum type subclass
 */
public interface CodeEnum<E extends Enum<E>> {

    /**
     *
     * @return
     */
    int getCode();

}
