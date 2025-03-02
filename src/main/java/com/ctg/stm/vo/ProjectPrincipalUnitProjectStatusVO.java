package com.ctg.stm.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectPrincipalUnitProjectStatusVO {
    @ApiModelProperty("主管单位")
    private String projectPrincipalUnit;

    @ApiModelProperty("流程状态")
    private String projectStatus;



    @ApiModelProperty("统计结果")
    private Long count;
}
