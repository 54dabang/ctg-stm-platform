package com.ctg.stm.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectResultCountGroupByProjectCategoryVO {
    @ApiModelProperty("项目层级 (国家级/省部级/集团级/子企业)")
    private String projectCategory ;

    @ApiModelProperty("对应状态的项目数量")
    private Long count;

}
