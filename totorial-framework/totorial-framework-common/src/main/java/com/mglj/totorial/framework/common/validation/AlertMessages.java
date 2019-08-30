package com.mglj.totorial.framework.common.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by zsp on 2018/8/7.
 */
@Target({ElementType.FIELD})
@Retention(RUNTIME)
public @interface AlertMessages {

    AlertMessage[] value();

}
