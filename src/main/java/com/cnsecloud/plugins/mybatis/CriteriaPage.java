package com.cnsecloud.plugins.mybatis;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cnsecloud.plugins.mybatis.annotation.QueryEntity;
import com.cnsecloud.plugins.mybatis.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ZhangHongYuan
 * @date 2020/5/17
 */
public interface CriteriaPage {

    /**
     * 生成MybatisPlus分页对象，分页对象中包括排序
     *
     * @param pagination 分页参数
     * @param <T> 分页中的实体对象
     * @return 分页
     */
    default <T> Page<T> page(Pagination pagination) {
        Page<T> page = new Page<>(pagination.getPage(), pagination.getPageSize());
        List<String> sort = pagination.getSort();
        QueryEntity annotation = this.getClass().getAnnotation(QueryEntity.class);
        if (annotation != null && sort != null && sort.size() > 0) {
            // 使用MyBatis列与实体映射缓存,key为大写
            Map<String, ColumnCache> columnMap = LambdaUtils.getColumnMap(annotation.value());
            if (columnMap != null && !columnMap.isEmpty()) {
                List<OrderItem> orderItemList = new ArrayList<>();
                for (String item : sort) {
                    if (Utils.isNotBlank(item)) {
                        String[] split = item.split(",");
                        if (Utils.isNotBlank(split[0])) {
                            ColumnCache columnCache = columnMap.get(split[0].toUpperCase());
                            if (columnCache != null) {
                                String column = columnCache.getColumn();
                                if (split.length > 1) {
                                    if ("DESC".equalsIgnoreCase(split[1])) {
                                        orderItemList.add(OrderItem.desc(column));
                                    } else {
                                        orderItemList.add(OrderItem.asc(column));
                                    }
                                } else {
                                    orderItemList.add(OrderItem.asc(column));
                                }
                            }
                        }
                    }
                }
                if (orderItemList.size() > 0) {
                    page.addOrder(orderItemList);
                }
            }
        }
        return page;
    }
}
