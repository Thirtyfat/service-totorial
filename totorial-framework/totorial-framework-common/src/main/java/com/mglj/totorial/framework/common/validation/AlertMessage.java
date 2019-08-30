package com.mglj.totorial.framework.common.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by zsp on 2018/8/7.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
@Repeatable(AlertMessages.class)
public @interface AlertMessage {

    String code();

    boolean enableCodeMask() default true;

    String msg() default "数据错误";

}
