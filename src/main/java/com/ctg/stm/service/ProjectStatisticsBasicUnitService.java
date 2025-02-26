package com.ctg.stm.service;

import com.ctg.stm.domain.ProjectStatisticsBasicUnit;
import com.ctg.stm.dto.MonthlyScientificResearchReportQueryDTO;

import javax.persistence.criteria.Predicate;
import java.util.List;


public interface ProjectStatisticsBasicUnitService {
     ProjectStatisticsBasicUnit save(ProjectStatisticsBasicUnit unit);

     List<ProjectStatisticsBasicUnit> findByMonthlyScientificResearchReportQueryDTO(MonthlyScientificResearchReportQueryDTO queryDTO, PredicateCallBack predicateCallBack);

     List<ProjectStatisticsBasicUnit> findProjectUnderDevelopment(MonthlyScientificResearchReportQueryDTO queryDTO);

     List<ProjectStatisticsBasicUnit> findAllProjectStatisticsBasicUnits();
}
