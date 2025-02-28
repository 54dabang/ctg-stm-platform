package com.ctg.stm.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectCountGroupByProjectStatusVO {
  /*  @ApiModelProperty("项目状态（立项中/执行中/验收阶段）")
    private Integer proBpmStatus;*/

    @ApiModelProperty("项目状态（立项中/执行中/验收阶段）")
    private String projectStatus;

    @ApiModelProperty("对应状态的项目数量")
    private Long count;
}