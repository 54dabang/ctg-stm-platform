package com.ctg.stm.service;

import com.ctg.stm.domain.Statistics;
import com.ctg.stm.dto.MonthlyScientificResearchReportQueryDTO;
import com.ctg.stm.dto.BpmStatusGroupDTO;
import com.ctg.stm.dto.ProjectCategoryGroupDTO;

import java.util.List;

public interface StatisticsService {

    Statistics save(Statistics unit);

    List<Statistics> findByMonthlyScientificResearchReportQueryDTO(MonthlyScientificResearchReportQueryDTO queryDTO, PredicateCallBack predicateCallBack);

    List<Statistics> findProjectUnderDevelopment(MonthlyScientificResearchReportQueryDTO queryDTO);

    List<Statistics> findAllStatistics();

    List<BpmStatusGroupDTO> groupByProBpmStatus(MonthlyScientificResearchReportQueryDTO queryDTO);

    List<ProjectCategoryGroupDTO> groupByProjectCategoryUnderDevelopment(MonthlyScientificResearchReportQueryDTO queryDTO);

}
