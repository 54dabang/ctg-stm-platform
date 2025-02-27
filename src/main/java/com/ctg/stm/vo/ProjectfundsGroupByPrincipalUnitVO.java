package com.ctg.stm.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProjectfundsGroupByPrincipalUnitVO {
    @ApiModelProperty("主管单位")
    private String projectPrincipalUnit;

    @ApiModelProperty("研发投入")
    private BigDecimal projectfunds;
}
