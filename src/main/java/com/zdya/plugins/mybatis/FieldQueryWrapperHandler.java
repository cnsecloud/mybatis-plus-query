package com.zdya.plugins.mybatis;

import com.baomidou.mybatisplus.core.conditions.interfaces.Compare;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.zdya.plugins.mybatis.annotation.Query;
import com.zdya.plugins.mybatis.util.Utils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author ZhangHongYuan
 * @date 2020/5/17
 */
public class FieldQueryWrapperHandler {

    private Object criteria;
    private List<String> columns = new ArrayList<>();
    private Map<String, ColumnCache> columnMap;
    private QueryWrapper<?> queryWrapper;
    private Object filedValue;

    public void and(Field field) {
        if (criteria == null) {
            return;
        }
        if (queryWrapper == null) {
            return;
        }
        if (columnMap == null) {
            return;
        }
        boolean accessible = field.isAccessible();
        Object fieldValue = null;
        try {
            fieldValue = Utils.getFieldValue(criteria, field);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (fieldValue == null) {
            return;
        }
        if(fieldValue instanceof String){
            if(Utils.isBlank((String) fieldValue)){
                return;
            }
        }
        if(fieldValue instanceof Collection){
            if(Utils.isEmpty((Collection<?>) fieldValue)){
                return;
            }
        }
        Query annotation = field.getAnnotation(Query.class);
        if (annotation == null) {
            return;
        }
        List<String> columnNames = parseColumnNames(field, annotation);
        if (columnNames.isEmpty()) {
            return;
        }
        this.columns = columnNames;
        this.filedValue = fieldValue;
        typeAnd(annotation);
        field.setAccessible(accessible);
    }

    private void handle(WrapperOnceRunnable one) {
        int size = this.columns.size();
        if (size == 1) {
            one.run(this.queryWrapper, this.columns.get(0), filedValue);
        } else if (size > 1) {
            this.queryWrapper.and(andQueryWrapper ->
                    this.columns.forEach(column -> {
                        andQueryWrapper.or();
                        one.run(andQueryWrapper, column, filedValue);
                    }));
        }
    }


    private void typeAnd(Query query) {
        switch (query.type()) {
            case NE:
                this.handle(Compare::ne);
                break;
            case GT:
                this.handle(Compare::gt);
                break;
            case GE:
                this.handle(Compare::ge);
                break;
            case LT:
                this.handle(Compare::lt);
                break;
            case LE:
                this.handle(Compare::le);
                break;
            case BETWEEN:
                this.handle((andQueryWrapper, column, value) -> {
                    Object[] objects = ((Collection<?>) value).toArray();
                    andQueryWrapper.between(column, objects[0], objects[1]);
                });
                break;
            case NOT_BETWEEN:
                this.handle((andQueryWrapper, column, value) -> {
                    Object[] objects = ((Collection<?>) value).toArray();
                    andQueryWrapper.notBetween(column, objects[0], objects[1]);
                });
                break;
            case LIKE:
                this.handle(Compare::like);
                break;
            case NOT_LIKE:
                this.handle(Compare::notLike);
                break;
            case LIKE_LEFT:
                this.handle(Compare::likeLeft);
                break;
            case LIKE_RIGHT:
                this.handle(Compare::likeRight);
                break;
            case IS_NULL:
                this.handle((andQueryWrapper, column, value) -> {
                    if ((Boolean) value) {
                        andQueryWrapper.isNull(column);
                    }
                });
                break;
            case IS_NOT_NULL:
                this.handle((andQueryWrapper, column, value) -> {
                    if ((Boolean) value) {
                        andQueryWrapper.isNotNull(column);
                    }
                });
                break;
            case IN:
                this.handle((andQueryWrapper, column, finalValue) -> {
                    if (((Collection<?>) finalValue).size() > 0) {
                        andQueryWrapper.in(column, (Collection<?>) finalValue);
                    }
                });
                break;
            case NOT_IN:
                this.handle((andQueryWrapper, column, finalValue) -> {
                    if (((Collection<?>) finalValue).size() > 0) {
                        andQueryWrapper.notIn(column, (Collection<?>) finalValue);
                    }
                });
                break;
            default:
                this.handle(Compare::eq);
                break;
        }

    }

    private List<String> parseColumnNames(Field field, Query annotation) {
        List<String> propNames = new ArrayList<>();
        String[] strings = annotation.moreProp();
        if (strings.length < 1) {
            if (Utils.isNotBlank(annotation.propName())) {
                propNames.add(annotation.propName());
            } else {
                propNames.add(field.getName());
            }
        } else {
            propNames.addAll(Arrays.asList(strings));
        }
        List<String> columns = new ArrayList<>();
        propNames.forEach(propName -> {
            ColumnCache columnCache = columnMap.get(propName.toUpperCase());
            if (columnCache != null) {
                columns.add(columnCache.getColumn());
            }
        });
        return columns;
    }

    public FieldQueryWrapperHandler(Object criteria, QueryWrapper<?> queryWrapper, Map<String, ColumnCache> columnMap) {
        this.criteria = criteria;
        this.queryWrapper = queryWrapper;
        this.columnMap = columnMap;
    }
}
