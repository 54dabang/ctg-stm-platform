package com.ctg.stm.service.impl;

import com.ctg.stm.domain.ProjectStatisticsBasicUnit;
import com.ctg.stm.dto.MonthlyScientificResearchReportQueryDTO;
import com.ctg.stm.repository.ProjectStatisticsBasicUnitRepository;
import com.ctg.stm.service.PredicateCallBack;
import com.ctg.stm.service.ProjectStatisticsBasicUnitService;
import com.ctg.stm.util.ProjectEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class ProjectStatisticsBasicUnitServiceImpl implements ProjectStatisticsBasicUnitService {
    @Autowired
    private ProjectStatisticsBasicUnitRepository projectStatisticsBasicUnitRepository;


    @Override
    public List<ProjectStatisticsBasicUnit> findAllProjectStatisticsBasicUnits() {
        return projectStatisticsBasicUnitRepository.findAll();
    }

    @Override
    public ProjectStatisticsBasicUnit save(ProjectStatisticsBasicUnit unit) {
        return projectStatisticsBasicUnitRepository.save(unit);
    }

    @Override
    public List<ProjectStatisticsBasicUnit> findByMonthlyScientificResearchReportQueryDTO(
            MonthlyScientificResearchReportQueryDTO queryDTO, PredicateCallBack predicateCallBack) {

        Specification<ProjectStatisticsBasicUnit> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 日期范围查询（结合网页5[5](@ref)的日期处理）
            if (queryDTO.getStartDate() != null && queryDTO.getEndDate() != null) {
                predicates.add(cb.between(root.get("statisticDate"),
                        queryDTO.getStartDate(),
                        queryDTO.getEndDate()));
            }

            // 字符串类型精确匹配（参考网页2[2](@ref)的@NotBlank处理）
            addIfNotEmpty(predicates, root, cb, "principalUnit", queryDTO.getPrincipalUnit());
            addIfNotEmpty(predicates, root, cb, "businessSector", queryDTO.getBusinessSector());
            addIfNotEmpty(predicates, root, cb, "researchAttributes", queryDTO.getResearchAttributes());
           // addIfNotEmpty(predicates, root, cb, "projectCategory", queryDTO.getProjectCategory());

            // 数值类型精确匹配（类似网页3[3](@ref)的@Min处理）
            if (queryDTO.getProjectLevel() != null) {
                predicates.add(cb.equal(root.get("projectLevel"), queryDTO.getProjectLevel()));
            }
            if (predicateCallBack != null && !CollectionUtils.isEmpty(predicateCallBack.toPredicates(root, query, cb))) {
                List<Predicate> predicateList = predicateCallBack.toPredicates(root, query, cb);
                for (Predicate predicate : predicateList) {
                    predicates.add(predicate);
                }
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return projectStatisticsBasicUnitRepository.findAll(spec);
    }

    @Override
    public List<ProjectStatisticsBasicUnit> findProjectUnderDevelopment(MonthlyScientificResearchReportQueryDTO queryDTO) {

        PredicateCallBack predicateCallBack = (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();

            Predicate inPredicate = root.get("bpmStatus")
                    .in(ProjectEnum.ProBpmStatus.PROCESSING.desc(), ProjectEnum.ProBpmStatus.PROCESSING_ACCEPTANCE.desc());
            predicateList.add(inPredicate);
            return predicateList;
        };
        return findByMonthlyScientificResearchReportQueryDTO(queryDTO, predicateCallBack);

    }

    // 封装字符串字段的非空判断（参考网页7[7](@ref)的条件构建模式）
    private void addIfNotEmpty(List<Predicate> predicates, Root<?> root,
                               CriteriaBuilder cb, String fieldName, String value) {
        if (StringUtils.hasText(value)) {
            predicates.add(cb.equal(root.get(fieldName), value));
        }
    }
}