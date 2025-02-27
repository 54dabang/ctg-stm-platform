package com.ctg.stm.service;

import com.ctg.stm.domain.Statistics;
import com.ctg.stm.dto.MonthlyScientificResearchReportQueryDTO;
import com.ctg.stm.vo.*;

import java.math.BigDecimal;
import java.util.List;

public interface StatisticsService {

    Statistics save(Statistics unit);

    List<Statistics> findByMonthlyScientificResearchReportQueryDTO(MonthlyScientificResearchReportQueryDTO queryDTO, PredicateCallBack predicateCallBack);

    List<Statistics> findProjectUnderDevelopment(MonthlyScientificResearchReportQueryDTO queryDTO);

    List<Statistics> findAllStatistics();

    List<ProjectCountGroupByBpmStatusVO> countProjectNumGroupByProBpmStatus(MonthlyScientificResearchReportQueryDTO queryDTO);

    List<ProjectCountGroupByProjectCategoryVO> countProjectNumGroupByProjectCategoryUnderDevelopment(MonthlyScientificResearchReportQueryDTO queryDTO);

    List<ProjectResultCountGroupByProjectCategoryVO> countProjectResultGroupByProjectCategory(MonthlyScientificResearchReportQueryDTO queryDTO);

    List<Statistics> findImportantProjectTotalFunds(MonthlyScientificResearchReportQueryDTO queryDTO);

    Long countProjectNumUnderDevelopment(MonthlyScientificResearchReportQueryDTO queryDTO);

    Long countProjectNumUnderAcceptance(MonthlyScientificResearchReportQueryDTO queryDTO);

    Integer sumProjectResult(MonthlyScientificResearchReportQueryDTO queryDTO);
    BigDecimal sumProjectTotalFunds(MonthlyScientificResearchReportQueryDTO queryDTO);


    List<ProjectPrincipalUnitBpmStatusVO> countProjectNumGroupByPrincipalUnitAndProBpmStatus(MonthlyScientificResearchReportQueryDTO queryDTO);

    List<ProjectfundsGroupByPrincipalUnitVO> countProjectTotalFundsGroupByPrincipalUnit(MonthlyScientificResearchReportQueryDTO queryDTO);

}
