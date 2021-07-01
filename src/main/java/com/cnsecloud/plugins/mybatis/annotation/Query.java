package com.cnsecloud.plugins.mybatis.annotation;

import com.cnsecloud.plugins.mybatis.QueryType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ZhangHongYuan
 * @date 2020-05-16
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

    /**
     * 支持多个字段。优先级比propName 高，会覆盖propName内容
     *
     * @return 查询属性名
     */
    String[] moreProp() default {};

    /**
     * 属性名，如果为空去被注解的属性名称
     *
     * @return 属性名
     */
    String propName() default "";

    /**
     * 查询条件类型
     *
     * @return 查询条件类型
     */
    QueryType type() default QueryType.EQ;

}

