package com.ctg.stm.service.impl;

import com.ctg.stm.domain.Statistics;
import com.ctg.stm.dto.MonthlyScientificResearchReportQueryDTO;
import com.ctg.stm.vo.ProjectCountGroupByBpmStatusVO;
import com.ctg.stm.vo.ProjectCountGroupByProjectCategoryVO;
import com.ctg.stm.repository.StatisticsRepository;
import com.ctg.stm.service.PredicateCallBack;
import com.ctg.stm.service.StatisticsService;
import com.ctg.stm.util.ProjectEnum;
import com.ctg.stm.vo.ProjectResultCountGroupByProjectCategoryVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private EntityManager entityManager;
    @Override
    public Statistics save(Statistics unit) {
        return statisticsRepository.save(unit);
    }

    @Override
    public List<Statistics> findByMonthlyScientificResearchReportQueryDTO(MonthlyScientificResearchReportQueryDTO queryDTO, PredicateCallBack predicateCallBack) {
        Specification<Statistics> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 日期范围查询
            if (queryDTO.getStartDate() != null && queryDTO.getEndDate() != null) {
                predicates.add(cb.between(root.get("statisticDate"),
                        queryDTO.getStartDate(),
                        queryDTO.getEndDate()));
            }

            // 字符串类型精确匹配
            addIfNotEmpty(predicates, root, cb, "principalUnit", queryDTO.getPrincipalUnit());
            addIfNotEmpty(predicates, root, cb, "businessSector", queryDTO.getBusinessSector());
            addIfNotEmpty(predicates, root, cb, "researchAttribute", queryDTO.getResearchAttribute());
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

        return statisticsRepository.findAll(spec);
    }

    @Override
    public List<Statistics> findProjectUnderDevelopment(MonthlyScientificResearchReportQueryDTO queryDTO) {
        PredicateCallBack predicateCallBack = (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();

            Predicate inPredicate = root.get("bpmStatus")
                    .in(ProjectEnum.ProBpmStatus.PROCESSING.desc(), ProjectEnum.ProBpmStatus.PROCESSING_ACCEPTANCE.desc());
            predicateList.add(inPredicate);
            return predicateList;
        };
        return findByMonthlyScientificResearchReportQueryDTO(queryDTO, predicateCallBack);
    }

    @Override
    public List<Statistics> findAllStatistics() {
        return statisticsRepository.findAll();
    }


    private void addIfNotEmpty(List<Predicate> predicates, Root<?> root,
                               CriteriaBuilder cb, String fieldName, String value) {
        if (StringUtils.hasText(value)) {
            predicates.add(cb.equal(root.get(fieldName), value));
        }
    }

    @Override
    public List<ProjectCountGroupByProjectCategoryVO> coutProjectNumGroupByProjectCategoryUnderDevelopment(MonthlyScientificResearchReportQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Statistics> root = cq.from(Statistics.class);

        // 构建统计字段
        cq.multiselect(
                root.get("projectCategory"),  // 分组字段
                cb.count(root.get("id"))     // 统计数量
        );

        // 动态条件构建
        List<Predicate> predicates = buildPredicates(root,cb,queryDTO);
        Predicate inPredicate = root.get("bpmStatus")
                .in(ProjectEnum.ProBpmStatus.PROCESSING.value(), ProjectEnum.ProBpmStatus.PROCESSING_ACCEPTANCE.value());
        predicates.add(inPredicate);
        cq.where(predicates.toArray(new Predicate[0]));

        // 分组配置
        cq.groupBy(root.get("projectCategory"));

        // 执行数据库层面分组查询
        List<Object[]> results = entityManager.createQuery(cq).getResultList();
        // 结果转换
        return results.stream()
                .map(arr -> new ProjectCountGroupByProjectCategoryVO(
                        (String) arr[0], (Long)arr[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectResultCountGroupByProjectCategoryVO> coutProjectResultGroupByProjectCategory(MonthlyScientificResearchReportQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Statistics> root = cq.from(Statistics.class);

        // 构建统计字段
        cq.multiselect(
                root.get("projectCategory"),  // 分组字段
                cb.sum(root.get("projectResult"))     // 统计数量
        );

        // 动态条件构建
        List<Predicate> predicates = buildPredicates(root,cb,queryDTO);
        cq.where(predicates.toArray(new Predicate[0]));
        // 分组配置
        cq.groupBy(root.get("projectCategory"));
        // 执行数据库层面分组查询
        List<Object[]> results = entityManager.createQuery(cq).getResultList();
        // 结果转换
        return results.stream()
                .map(arr -> new ProjectResultCountGroupByProjectCategoryVO(
                        (String) arr[0], (Long)arr[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectCountGroupByBpmStatusVO> coutProjectNumGroupByProBpmStatus(MonthlyScientificResearchReportQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Statistics> root = cq.from(Statistics.class);

        // 构建统计字段
        cq.multiselect(
                root.get("bpmStatus"),  // 分组字段
                cb.count(root.get("id"))     // 统计数量
        );

        // 动态条件构建
        List<Predicate> predicates = buildPredicates(root,cb,queryDTO);
        cq.where(predicates.toArray(new Predicate[0]));

        // 分组配置
        cq.groupBy(root.get("bpmStatus"));

        // 执行数据库层面分组查询
        List<Object[]> results = entityManager.createQuery(cq).getResultList();
        // 结果转换
        return results.stream()
                .map(arr -> new ProjectCountGroupByBpmStatusVO(
                        (Integer) arr[0], (Long)arr[1]))
                .collect(Collectors.toList());
    }
    private  List<Predicate> buildPredicates(Root<?> root,CriteriaBuilder cb,MonthlyScientificResearchReportQueryDTO queryDTO){
        List<Predicate> predicates = Lists.newArrayList();
        addIfNotEmpty(predicates, root, cb, "principalUnit", queryDTO.getPrincipalUnit());
        addIfNotEmpty(predicates, root, cb, "businessSector", queryDTO.getBusinessSector());
        addIfNotEmpty(predicates, root, cb, "researchAttributes", queryDTO.getResearchAttribute());

        return predicates;
    }
}
