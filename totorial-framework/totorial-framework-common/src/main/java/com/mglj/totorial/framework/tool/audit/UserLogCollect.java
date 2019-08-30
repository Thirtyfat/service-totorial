package com.mglj.totorial.framework.tool.audit;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by zsp on 2018/8/30.
 */
@Target({ METHOD})
@Retention(RUNTIME)
public @interface UserLogCollect {

    String title() default "unnamed";

    String group() default "unnamed";

}
