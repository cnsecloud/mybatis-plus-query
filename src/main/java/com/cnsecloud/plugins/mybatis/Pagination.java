package com.cnsecloud.plugins.mybatis;


import java.util.List;

/**
 * @author ZhangHongYuan
 * @date 2020/5/17
 */
public class Pagination {

    public static final int DEFAULT_SIZE = 10;

    private int page = 0;
    private int pageSize = DEFAULT_SIZE;
    /**
     * 排序,格式：["item,asc","item2,desc","item3,asc"]
     */
    private List<String> sort;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
