package com.mglj.totorial.framework.tool.op;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by zsp on 2018/11/8.
 */
@Target({ElementType.METHOD})
@Retention(RUNTIME)
public @interface FrequentlyOperationPrevent {
}
