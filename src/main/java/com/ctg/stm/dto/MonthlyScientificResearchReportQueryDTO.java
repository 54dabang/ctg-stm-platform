package com.ctg.stm.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class MonthlyScientificResearchReportQueryDTO {
    private LocalDate startDate;

    private LocalDate endDate;

    @ApiModelProperty(value = "主管单位 责任单位")
    private String principalUnit;

    @ApiModelProperty(value = "项目所属领域 对应业务板块（水电、新能源、火电）")
    @Column(name = "business_sector")
    private String businessSector;

    @ApiModelProperty(value = "研发属性 基础研究 应用基础研究")
    private String researchAttributes;


    @ApiModelProperty(value = "项目层级 项目类别 国家级")
    private String projectCategory;


    @ApiModelProperty(value = "项目级别，依据totalFunds划分为大型、中型、小型")
    private Integer projectLevel;


}
