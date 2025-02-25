package com.ctg.stm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "MA_PROJECT_STATISTICS_BASIC_UNIT")
public class ProjectStatisticsBasicUnit implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "项目名称")
    @Column(name = "project_name")
    private String ProjectName;

    @ApiModelProperty(value = "主管单位 责任单位")
    @Column(name = "principal_unit")
    private String principalUnit;

    @ApiModelProperty(value = "项目所属领域 对应业务板块（水电、新能源、火电）")
    @Column(name = "business_sector")
    private String businessSector;

    @ApiModelProperty(value = "研发属性 基础研究 应用基础研究")
    @Column(name = "research_attributes")
    private String researchAttributes;


    @ApiModelProperty(value = "项目层级 项目类别 国家级")
    @Column(name = "project_category")
    private String projectCategory;

    @ApiModelProperty(value = "项目金额")
    @Column(name = "total_funds")
    private BigDecimal totalFunds;

    @ApiModelProperty(value = "项目级别，依据totalFunds划分为大型、中型、小型")
    @Column(name = "project_level")
    private Integer projectLevel;

    @ApiModelProperty(value = "项目类别，自主科研项目、外联科研项目")
    @Column(name = "project_type")
    private String projectType;

    @ApiModelProperty(value = "项目阶段，立项中、执行中、验收")
    @Column(name = "项目阶段")
    private String bpmStatus;


    @Column(name = "statistic_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate statisticDate;



}
