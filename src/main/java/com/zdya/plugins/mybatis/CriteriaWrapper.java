package com.zdya.plugins.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * @author ZhangHongYuan
 * @date 2020/5/17
 */
public interface CriteriaWrapper {


    /**
     * 查询条件封装
     *
     * @param <T> 对应实体类型
     * @return 条件封装
     */
    default <T> QueryWrapper<T> queryWrapper() {
        return QueryWrapperAndHelper.createCriteriaWrapper(this);
    }
}
