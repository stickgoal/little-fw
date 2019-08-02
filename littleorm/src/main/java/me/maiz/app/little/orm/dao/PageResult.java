package me.maiz.app.little.orm.dao;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<E> {
    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 总数据量
     */
    private int totalCount;

    /**
     * 页数
     */
    private int pageSize;

    /**
     * 当前页码，从0开始
     */
    private int currIndex;

    /**
     * 数据内容
     */
    private List<E> content;

}
