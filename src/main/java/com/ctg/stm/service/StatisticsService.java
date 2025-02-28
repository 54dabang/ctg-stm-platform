package com.ctg.stm.service;

import com.ctg.stm.domain.Statistics;
import com.ctg.stm.dto.MonthlyScientificResearchReportQueryDTO;
import com.ctg.stm.dto.ProjectQueryDetailDTO;
import com.ctg.stm.dto.StatisticsSearchDTO;
import com.ctg.stm.vo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

public interface StatisticsService {

    Statistics save(Statistics unit);

    Statistics findOne(Long id);

    List<Statistics> findByMonthlyScientificResearchReportQueryDTO(MonthlyScientificResearchReportQueryDTO queryDTO, PredicateCallBack predicateCallBack);

    List<Statistics> findProjectUnderDevelopment(MonthlyScientificResearchReportQueryDTO queryDTO);

    List<Statistics> findAllStatistics();

    List<ProjectCountGroupByProjectStatusVO> countProjectNumGroupByProBpmStatus(MonthlyScientificResearchReportQueryDTO queryDTO);

    List<ProjectCountGroupByProjectCategoryVO> countProjectNumGroupByProjectCategoryUnderDevelopment(MonthlyScientificResearchReportQueryDTO queryDTO);

    List<ProjectResultCountGroupByProjectCategoryVO> countProjectResultGroupByProjectCategory(MonthlyScientificResearchReportQueryDTO queryDTO);

    List<Statistics> findImportantProjectTotalFunds(MonthlyScientificResearchReportQueryDTO queryDTO);

    Long countProjectNumUnderDevelopment(MonthlyScientificResearchReportQueryDTO queryDTO);

    Long countProjectNumUnderAcceptance(MonthlyScientificResearchReportQueryDTO queryDTO);

    Integer sumProjectResult(MonthlyScientificResearchReportQueryDTO queryDTO);
    BigDecimal sumProjectTotalFunds(MonthlyScientificResearchReportQueryDTO queryDTO);


    List<ProjectPrincipalUnitProjectStatusVO> countProjectNumGroupByPrincipalUnitAndProBpmStatus(MonthlyScientificResearchReportQueryDTO queryDTO);

    List<ProjectfundsGroupByPrincipalUnitVO> countProjectTotalFundsGroupByPrincipalUnit(MonthlyScientificResearchReportQueryDTO queryDTO);

    List<ImportantProjectCountGroupByRankVO> countImportantProjectNumGroupByRank();

    List<ImportantProjectSumFundsGroupByRankVO> countImportantProjectSumFundsGroupByRank();
    Page<Statistics> findAll(Pageable pageable, Specification specification);

    Page<Statistics> importantProjectPage(StatisticsSearchDTO searchDTO);

    Page<Statistics> statisticspage(ProjectQueryDetailDTO projectQueryDetailDTO);
}
