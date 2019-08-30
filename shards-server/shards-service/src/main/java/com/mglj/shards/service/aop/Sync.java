package com.mglj.shards.service.aop;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by zsp on 2019/3/20.
 */
@Target({ METHOD })
@Retention(RUNTIME)
public @interface Sync {
}
