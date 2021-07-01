package com.zdya.plugins.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.zdya.plugins.mybatis.annotation.QueryEntity;
import com.zdya.plugins.mybatis.util.Utils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author ZhangHongYuan
 * @date 2020/5/17
 */
public class QueryWrapperAndHelper {

    public static <T> QueryWrapper<T> createCriteriaWrapper(Object criteria) {
        if (criteria == null) {
            return Wrappers.emptyWrapper();
        }
        Class<?> criteriaClass = criteria.getClass();
        QueryEntity queryEntity = criteriaClass.getAnnotation(QueryEntity.class);
        if (queryEntity == null) {
            return Wrappers.emptyWrapper();
        }

        Class<?> entityClass = queryEntity.value();
        Map<String, ColumnCache> columnMap = LambdaUtils.getColumnMap(entityClass);
        if (columnMap == null || columnMap.isEmpty()) {
            return Wrappers.emptyWrapper();
        }
        QueryWrapper<T> queryWrapper = Wrappers.query();
        List<Field> allFields = Utils.getFields(criteriaClass);
        FieldQueryWrapperHandler fieldQueryWrapperHandler = new FieldQueryWrapperHandler(criteria, queryWrapper, columnMap);
        allFields.forEach(fieldQueryWrapperHandler::and);
        return queryWrapper;
    }


}
