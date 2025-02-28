package com.ctg.stm.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVO<T> {
    // 总记录数
    private Long total;

    // 总页数
    private Integer pages;

    // 当前页码
    private Integer current;

    // 每页大小
    private Integer size;

    // 数据列表
    private List<T> records;
}