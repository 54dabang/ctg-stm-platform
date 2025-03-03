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

    @ApiModelProperty(value = "项目名ID")
    @Column(name = "PROJECT_ID")
    private String projectID;

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
    @Column(name = "RESEARCH_ATTRIBUTES")
    private String researchAttributes;

    @ApiModelProperty(value = "项目金额")
    @Column(name = "TOTAL_FUNDS")
    private BigDecimal totalFunds;

    @ApiModelProperty(value = "项目级别，依据totalFunds划分为大型、中型、小型")
    @Column(name = "PROJECT_LEVEL")
    private String projectLevel;

    @ApiModelProperty(value = "项目成果数量（累计）")
    @Column(name = "PROJECT_RESULT")
    private Integer projectResult;

    @ApiModelProperty(value = "项目成果数量（专利）")
    @Column(name = "RESULT_ZL")
    private Integer resultZL;

    @ApiModelProperty(value = "项目成果数量（软著）")
    @Column(name = "RESULT_RZ")
    private Integer resultRZ;

    @ApiModelProperty(value = "项目成果数量（论文）")
    @Column(name = "RESULT_LW")
    private Integer resultLW;

    @ApiModelProperty(value = "项目成果数量（标准）")
    @Column(name = "RESULT_BZ")
    private Integer resultBZ;

    @ApiModelProperty(value = "项目成果数量（专著）")
    @Column(name = "RESULT_ZZ")
    private Integer resultZZ;



//    @ApiModelProperty(value = "项目成果（近1月）")
//    @Column(name = "PROJECT_RESULT_1")
//    private int projectResult1;
//
//    @ApiModelProperty(value = "项目成果（近3月）")
//    @Column(name = "PROJECT_RESULT_3")
//    private int projectResult3;
//
//    @ApiModelProperty(value = "项目成果（近12月）")
//    @Column(name = "PROJECT_RESULT_12")
//    private int projectResult12;

    @ApiModelProperty(value = "研发投入金额（累计）")
    @Column(name = "PROJECT_FUNDS")
    private BigDecimal projectfunds;

//    @ApiModelProperty(value = "研发投入（近1月）")
//    @Column(name = "PROJECT_FUNDS_1")
//    private BigDecimal projectfunds1;
//
//    @ApiModelProperty(value = "研发投入（近3月）")
//    @Column(name = "PROJECT_FUNDS_3")
//    private BigDecimal projectfunds3;
//
//    @ApiModelProperty(value = "研发投入（近12月）")
//    @Column(name = "PROJECT_FUNDS_12")
//    private BigDecimal projectfunds12;

    @ApiModelProperty(value = "项目执行（立项中、执行中、验收阶段）")
    @Column(name = "PROJECT_STATUS")
    private String projectStatus;

//    @ApiModelProperty(value = "近1月项目执行情况（1立项中、2执行中、3验收中）")
//    @Column(name = "PROJECT_STATUS_1")
//    private int projectStatus1;
//
//    @ApiModelProperty(value = "近3月项目执行情况（1立项中、2执行中、3验收中）")
//    @Column(name = "PROJECT_STATUS_3")
//    private int projectStatus3;


    @ApiModelProperty(value = "验收节点（待获取节点信息）")
    @Column(name = "ACCEPTANCE_POINT")
    private String acceptancePoint;

    @ApiModelProperty(value = "是否重点项目")
    @Column(name = "PROJECT_IMPORTANT")
    private Integer projectImportant;

    @ApiModelProperty(value = "流程状态")
    @Column(name = "BPM_STATUS")
    //需要改成String
    private String bpmStatus;

    @ApiModelProperty(value = "项目所属领域 对应业务板块（水电、新能源、火电）")
    @Column(name = "BUSINESS_SECTOR")
    private String businessSector;

    @ApiModelProperty(value = "项目所属专业")
    @Column(name = "PROFESSIONAL")
    private String professional;

    @ApiModelProperty(value = "重点项目划分（国家级、省部级、怀柔实验室）")
    @Column(name = "PROJECT_RANK")
    private String projectRank;




}
