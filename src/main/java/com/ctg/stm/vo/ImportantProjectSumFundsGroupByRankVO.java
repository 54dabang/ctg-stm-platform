package com.ctg.stm.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class ImportantProjectSumFundsGroupByRankVO {
    @ApiModelProperty("项目评级")
    private String rank;

    @ApiModelProperty("对应项目评级的投入金额")
    private BigDecimal projectTotalFunds;
}
