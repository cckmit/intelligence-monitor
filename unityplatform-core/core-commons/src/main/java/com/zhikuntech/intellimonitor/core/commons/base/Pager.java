package com.zhikuntech.intellimonitor.core.commons.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页列表。
 * <p>
 * 用于传输后端进行分页的列表数据。
 */
public class Pager<T> implements Serializable {

    /**
     * 总记录数。
     */
    private Integer totalCount;

    /**
     * 当前页的记录列表。
     */
    private List<T> list;

    /**
     * 构造函数。
     *
     * @param totalCount 总记录数。
     * @param list       当前页的记录列表。
     */
    public Pager(int totalCount, List<T> list) {
        this.totalCount = totalCount;
        this.list = list;
    }

    /**
     * 构造函数。
     *
     * @param list       当前页的记录列表。
     */
    public Pager(List<T> list) {
        this.list = list;
        if (list == null) {
            this.totalCount = 0;
        } else {
            this.totalCount = list.size();
        }
    }

    public static <O> Pager<O> emptyPager() {
        return new Pager<>(new ArrayList<>());
    }

    /**
     * @return the totalCount
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * @param totalCount the totalCount to set
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * @return the list
     */
    public List<T> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<T> list) {
        this.list = list;
    }


}
