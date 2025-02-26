package com.ctg.stm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "STATISTICS")
@NoArgsConstructor
@Getter
@Setter
public class Statistics implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "项目ID")
    @Column(name = "PROJECT_ID")
    private Long projectId;

    @ApiModelProperty(value = "项目名")
    @Column(name = "PROJECT_NAME")
    private String projectName;

    @ApiModelProperty(value = "项目开始时间")
    @Column(name = "START_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @ApiModelProperty(value = "项目结束时间")
    @Column(name = "END_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;

    @ApiModelProperty(value = "责任单位")
    @Column(name = "PRINCIPAL_UNIT")
    private String principalUnit;

    @ApiModelProperty(value = "负责人")
    @Column(name = "PRINCIPAL_NAME")
    private String principalName;

    @ApiModelProperty(value = "项目层级（国家级、省部级、集团级、子企业级）")
    @Column(name = "PROJECT_CATEGORY")
    private String projectCategory;

    @ApiModelProperty(value = "项目类别（自主、外联）")
    @Column(name = "PROJECT_TYPE")
    private String projectType;

    @ApiModelProperty(value = "研发属性（基础研究、应用基础研究、应用研究、技术与产品开发（试验发展、软科学研究）、其它")
    @Column(name = "RESEARCH_ATTRIBUTE")
    private String researchAttribute;

    @ApiModelProperty(value = "项目金额")
    @Column(name = "TOTAL_FUNDS")
    private BigDecimal totalFunds;

    @ApiModelProperty(value = "项目级别，依据totalFunds划分为大型、中型、小型")
    @Column(name = "PROJECT_LEVEL")
    private String projectLevel;

    @ApiModelProperty(value = "项目成果数量（累计）")
    @Column(name = "PROJECT_RESULT")
    private Integer projectResult;



    @ApiModelProperty(value = "研发投入金额（累计）")
    @Column(name = "PROJECT_FUNDS")
    private BigDecimal projectfunds;



    @ApiModelProperty(value = "项目执行（立项中、执行中、验收阶段）")
    @Column(name = "PROJECT_STATUS")
    private String projectStatus;



    @ApiModelProperty(value = "验收节点（待获取节点信息）")
    @Column(name = "ACCEPTANCE_POINT")
    private Integer acceptancePoint;

    @ApiModelProperty(value = "是否重点项目")
    @Column(name = "PROJECT_IMPORTANT")
    private Integer projectImportant;

    @ApiModelProperty(value = "流程状态")
    @Column(name = "BPM_STATUS")
    private Integer bpmStatus;



}
