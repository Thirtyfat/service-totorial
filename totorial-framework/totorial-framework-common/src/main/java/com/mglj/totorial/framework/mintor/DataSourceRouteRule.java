package com.mglj.totorial.framework.mintor;


import com.mglj.totorial.framework.common.enums.DataSourceRoleEnum;

import java.lang.annotation.*;

/**
 * 数据源路由规则
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceRouteRule {

    /**
     * 数据源的读写角色，默认为写库。
     *
     * @return
     */
    DataSourceRoleEnum role() default DataSourceRoleEnum.WRITABLE;

    /**
     * 是否开启分片键的赋值指定，默认为不开启。
     *
     * 开启后，分析入参，根据shardingKeyFields的设置决定分片键。
     *
     * @return
     */
    boolean enableShardingKeyAssigned() default false;

    /**
     * 获取分片键的入参字段集合
     *
     * @return
     */
    String[] shardingKeyFields() default {};

}
