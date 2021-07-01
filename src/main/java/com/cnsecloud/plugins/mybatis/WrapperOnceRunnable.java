package com.cnsecloud.plugins.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * @author ZhangHongYuan
 * @date 2020/5/17
 */
public interface WrapperOnceRunnable {

    /**
     * 生成条件时，每次执行的条件拼接内容
     *
     * @param andQueryWrapper 条件对象
     * @param column          数据库对应列名
     * @param fieldValue      属性值
     */
    void run(QueryWrapper<?> andQueryWrapper, String column, Object fieldValue);

}
