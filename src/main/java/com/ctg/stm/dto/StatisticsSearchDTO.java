package com.ctg.stm.dto;

import lombok.Data;

@Data
public class StatisticsSearchDTO {
    // 当前页码（从0开始）
    private Integer current = 0;

    // 每页大小
    private Integer size = 10;

    // 排序字段（默认按ID排序）
    private String sort = "id";

    // 排序方向（默认升序）
    private String order = "asc";

    // 关键字查询（可选）
    private String keywords;
}
