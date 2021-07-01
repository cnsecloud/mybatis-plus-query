package com.zdya.plugins.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询条件对应的实体类
 *
 * @author ZhangHongYuan
 * @date 2020/5/16
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryEntity {
    /**
     * @return 实体类class
     */
    Class<?> value();
}
