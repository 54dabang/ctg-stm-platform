package com.ctg.stm.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImportantProjectCountGroupByRankVO {
    @ApiModelProperty("项目评级")
    private String rank;

    @ApiModelProperty("对应状态的项目数量")
    private Long count;
}
