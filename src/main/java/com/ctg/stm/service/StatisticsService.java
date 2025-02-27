package com.ctg.stm.service;

import com.ctg.stm.domain.Statistics;
import com.ctg.stm.dto.MonthlyScientificResearchReportQueryDTO;
import com.ctg.stm.vo.ProjectCountGroupByBpmStatusVO;
import com.ctg.stm.vo.ProjectCountGroupByProjectCategoryVO;
import com.ctg.stm.vo.ProjectResultCountGroupByProjectCategoryVO;

import java.util.List;

public interface StatisticsService {

    Statistics save(Statistics unit);

    List<Statistics> findByMonthlyScientificResearchReportQueryDTO(MonthlyScientificResearchReportQueryDTO queryDTO, PredicateCallBack predicateCallBack);

    List<Statistics> findProjectUnderDevelopment(MonthlyScientificResearchReportQueryDTO queryDTO);

    List<Statistics> findAllStatistics();

    List<ProjectCountGroupByBpmStatusVO> coutProjectNumGroupByProBpmStatus(MonthlyScientificResearchReportQueryDTO queryDTO);

    List<ProjectCountGroupByProjectCategoryVO> coutProjectNumGroupByProjectCategoryUnderDevelopment(MonthlyScientificResearchReportQueryDTO queryDTO);

    List<ProjectResultCountGroupByProjectCategoryVO> coutProjectResultGroupByProjectCategory(MonthlyScientificResearchReportQueryDTO queryDTO);

}
