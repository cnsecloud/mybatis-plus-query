package com.zdya.plugins.mybatis;

/**
 * <p>
 * 查询条件类型
 * </p>
 *
 * @author ZhangHongYuan
 * @date 2021/7/2
 */
public enum QueryType {

    /**
     * 等于 =
     */
    EQ,
    /**
     * 不等于 <>
     */
    NE,
    /**
     * 大于 >
     */
    GT,
    /**
     * 大于等于 >=
     */
    GE,
    /**
     * 小于 <
     */
    LT,
    /**
     * 小于等于 <=
     */
    LE,
    /**
     * BETWEEN 值1 AND 值2，被注解属性类型值必须为 Collection
     */
    BETWEEN,
    /**
     * NOT BETWEEN 值1 AND 值2，被注解属性类型必须为 Collection
     */
    NOT_BETWEEN,
    /**
     * LIKE '%值%'
     */
    LIKE,
    /**
     * NOT LIKE '%值%'
     */
    NOT_LIKE,
    /**
     * LIKE '%值'
     */
    LIKE_LEFT,
    /**
     * LIKE '值%'
     */
    LIKE_RIGHT,
    /**
     * 字段 IS NULL，被注解属性类型必须为 Boolean
     */
    IS_NULL,
    /**
     * 字段 IS NOT NULL，被注解属性类型必须为 Boolean
     */
    IS_NOT_NULL,

    /**
     * 字段 IN ，被注解属性类型必须为 Collection
     */
    IN,
    /**
     * 字段 IN (v0, v1, ...)，被注解属性类型必须为 Collection
     */
    NOT_IN

}
