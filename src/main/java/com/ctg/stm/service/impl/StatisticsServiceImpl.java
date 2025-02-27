package com.ctg.stm.service.impl;

import com.ctg.stm.domain.Statistics;
import com.ctg.stm.dto.MonthlyScientificResearchReportQueryDTO;
import com.ctg.stm.util.Constants;
import com.ctg.stm.vo.*;
import com.ctg.stm.repository.StatisticsRepository;
import com.ctg.stm.service.PredicateCallBack;
import com.ctg.stm.service.StatisticsService;
import com.ctg.stm.util.ProjectEnum;
import com.google.common.collect.Lists;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.*;
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
            return buildSpecification(root, query, cb, queryDTO, predicateCallBack);
        };

        return statisticsRepository.findAll(spec);
    }

    @Override
    public Long countProjectNumUnderDevelopment(MonthlyScientificResearchReportQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Statistics> root = cq.from(Statistics.class);

        // 构建统计字段
        cq.multiselect(
                cb.count(root.get("id"))     // 统计数量
        );

        // 动态条件构建
        List<Predicate> predicates = buildPredicates(root, cb, queryDTO);
        Predicate inPredicate = root.get("bpmStatus")
                .in(ProjectEnum.ProBpmStatus.PROCESSING.value(), ProjectEnum.ProBpmStatus.PROCESSING_ACCEPTANCE.value());
        predicates.add(inPredicate);
        cq.where(predicates.toArray(new Predicate[0]));


        // 执行数据库层面分组查询
        TypedQuery<Long> query = entityManager.createQuery(cq);
        return query.getSingleResult(); // 返回统计结果

    }

    @Override
    public Long countProjectNumUnderAcceptance(MonthlyScientificResearchReportQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Statistics> root = cq.from(Statistics.class);

        // 构建统计字段
        cq.multiselect(
                cb.count(root.get("id"))     // 统计数量
        );

        // 动态条件构建
        List<Predicate> predicates = buildPredicates(root, cb, queryDTO);
        Predicate inPredicate = root.get("bpmStatus")
                .in(ProjectEnum.ProBpmStatus.ACCEPTANCE.value());
        predicates.add(inPredicate);
        cq.where(predicates.toArray(new Predicate[0]));


        // 执行数据库层面分组查询
        TypedQuery<Long> query = entityManager.createQuery(cq);
        return query.getSingleResult(); // 返回统计结果
    }

    @Override
    public BigDecimal sumProjectTotalFunds(MonthlyScientificResearchReportQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
        Root<Statistics> root = cq.from(Statistics.class);

        // 构建统计字段
        cq.select(
                cb.sum(root.get("projectfunds"))
        );

        // 动态条件构建
        List<Predicate> predicates = buildPredicates(root, cb, queryDTO);
        cq.where(predicates.toArray(new Predicate[0]));

        // 执行数据库层面分组查询
        TypedQuery<BigDecimal> query = entityManager.createQuery(cq);
        return query.getSingleResult(); // 返回统计结果

    }

    @Override
    public Integer sumProjectResult(MonthlyScientificResearchReportQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
        Root<Statistics> root = cq.from(Statistics.class);

        // 构建统计字段
        cq.select(
                cb.sum(root.get("projectResult"))
        );

        // 动态条件构建
        List<Predicate> predicates = buildPredicates(root, cb, queryDTO);
        cq.where(predicates.toArray(new Predicate[0]));

        // 执行数据库层面分组查询
        TypedQuery<Integer> query = entityManager.createQuery(cq);
        return query.getSingleResult(); // 返回统计结果

    }

    @Override
    public List<ProjectPrincipalUnitBpmStatusVO> countProjectNumGroupByPrincipalUnitAndProBpmStatus(MonthlyScientificResearchReportQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Statistics> root = cq.from(Statistics.class);

        // 构建统计字段（分组字段 + 计数）
        cq.multiselect(
                root.get("principalUnit"),    // 分组字段2
                root.get("bpmStatus"),        // 分组字段1
                cb.count(root.get("id"))      // 统计数量
        );

        // 动态条件构建（保留原有过滤逻辑）
        List<Predicate> predicates = buildPredicates(root, cb, queryDTO);

        cq.where(predicates.toArray(new Predicate[0]));

        // 分组配置（双字段分组）
        cq.groupBy(
                root.get("bpmStatus"),
                root.get("principalUnit")
        );

        // 添加排序（可选）
        cq.orderBy(
                cb.asc(root.get("bpmStatus")),
                cb.asc(root.get("principalUnit"))
        );
        // 执行查询并转换结果
        return entityManager.createQuery(cq)
                .getResultList()
                .stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectfundsGroupByPrincipalUnitVO> countProjectTotalFundsGroupByPrincipalUnit(MonthlyScientificResearchReportQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Statistics> root = cq.from(Statistics.class);

        cq.multiselect(
                root.get("principalUnit"),
                cb.sum(root.get("projectfunds"))      // 累加项目太投入
        );

        // 动态条件构建（保留原有过滤逻辑）
        List<Predicate> predicates = buildPredicates(root, cb, queryDTO);

        cq.where(predicates.toArray(new Predicate[0]));

        cq.groupBy(
                root.get("principalUnit")
        );

        // 添加排序（可选）
        cq.orderBy(
                cb.asc(root.get("principalUnit"))
        );
        // 执行查询并转换结果
        return entityManager.createQuery(cq)
                .getResultList()
                .stream()
                .map(r->new ProjectfundsGroupByPrincipalUnitVO((String) r[0], Optional.ofNullable(r[1]).map(BigDecimal.class::cast).orElse(BigDecimal.ZERO)))
                .collect(Collectors.toList());
    }

    // 结果转换方法
    private ProjectPrincipalUnitBpmStatusVO convertToVO(Object[] result) {
        return new ProjectPrincipalUnitBpmStatusVO(
                (String) result[0],  //
                (Integer) result[1],  // bpmStatus
                (String) ProjectEnum.ProBpmStatus.getByValue((Integer) result[1]).desc(),
                (Long) result[2]     // count
        );
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
                               CriteriaBuilder cb, String fieldName, Object value) {
        if (Objects.nonNull(value)) {
            predicates.add(cb.equal(root.get(fieldName), value));
        }
    }

    @Override
    public List<ProjectCountGroupByProjectCategoryVO> countProjectNumGroupByProjectCategoryUnderDevelopment(MonthlyScientificResearchReportQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Statistics> root = cq.from(Statistics.class);

        // 构建统计字段
        cq.multiselect(
                root.get("projectCategory"),  // 分组字段
                cb.count(root.get("id"))     // 统计数量
        );

        // 动态条件构建
        List<Predicate> predicates = buildPredicates(root, cb, queryDTO);
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
                        (String) arr[0], (Long) arr[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectResultCountGroupByProjectCategoryVO> countProjectResultGroupByProjectCategory(MonthlyScientificResearchReportQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Statistics> root = cq.from(Statistics.class);

        // 构建统计字段
        cq.multiselect(
                root.get("projectCategory"),  // 分组字段
                cb.sum(root.get("projectResult"))     // 统计数量
        );

        // 动态条件构建
        List<Predicate> predicates = buildPredicates(root, cb, queryDTO);
        cq.where(predicates.toArray(new Predicate[0]));
        // 分组配置
        cq.groupBy(root.get("projectCategory"));
        // 执行数据库层面分组查询
        List<Object[]> results = entityManager.createQuery(cq).getResultList();
        // 结果转换
        return results.stream()
                .map(arr -> new ProjectResultCountGroupByProjectCategoryVO(
                        (String) arr[0], (Long) arr[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectCountGroupByBpmStatusVO> countProjectNumGroupByProBpmStatus(MonthlyScientificResearchReportQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Statistics> root = cq.from(Statistics.class);

        // 构建统计字段
        cq.multiselect(
                root.get("bpmStatus"),  // 分组字段
                cb.count(root.get("id"))     // 统计数量
        );

        // 动态条件构建
        List<Predicate> predicates = buildPredicates(root, cb, queryDTO);
        cq.where(predicates.toArray(new Predicate[0]));

        // 分组配置
        cq.groupBy(root.get("bpmStatus"));

        // 执行数据库层面分组查询
        List<Object[]> results = entityManager.createQuery(cq).getResultList();
        // 结果转换
        return results.stream()
                .map(arr -> new ProjectCountGroupByBpmStatusVO(
                        (Integer) arr[0], (Long) arr[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<Statistics> findImportantProjectTotalFunds(MonthlyScientificResearchReportQueryDTO queryDTO) {


        Specification<Statistics> spec = (root, query, cb) -> {
            PredicateCallBack predicateCallBack = new PredicateCallBack() {
                @Override
                public List<Predicate> toPredicates(Root rt, CriteriaQuery query, CriteriaBuilder cb) {
                    return Arrays.asList(cb.equal(root.get("projectImportant"),Integer.valueOf(Constants.ALL_YES)));
                }
            };
            return buildSpecification(root, query, cb, queryDTO, predicateCallBack);
        };
        // 创建 Pageable，指定排序和分页
        Sort sort = Sort.by(Sort.Order.desc("projectfunds")); // 按 age 字段降序排序
        Pageable pageable = PageRequest.of(0, 10, sort); // 第 0 页，每页 10 条

        Page<Statistics> page = statisticsRepository.findAll(spec, pageable);

        return page.toList();

    }

    @Override
    public List<ImportantProjectCountGroupByRankVO> countImportantProjectNumGroupByRank(MonthlyScientificResearchReportQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Statistics> root = cq.from(Statistics.class);

        // 构建统计字段
        cq.multiselect(
                root.get("rank"),  // 分组字段
                cb.count(root.get("id"))     // 统计数量
        );

        // 动态条件构建
        PredicateCallBack predicateCallBack = new PredicateCallBack() {
            @Override
            public List<Predicate> toPredicates(Root rt, CriteriaQuery query, CriteriaBuilder cb) {
                return Arrays.asList(cb.equal(root.get("projectImportant"),Integer.valueOf(Constants.ALL_YES)));
            }
        };

        Predicate predicate = buildSpecification(root,cq, cb, queryDTO,predicateCallBack);
        cq.where(predicate);

        // 分组配置
        cq.groupBy(root.get("rank"));

        // 执行数据库层面分组查询
        List<Object[]> results = entityManager.createQuery(cq).getResultList();
        // 结果转换
        return results.stream()
                .map(arr -> new ImportantProjectCountGroupByRankVO(
                        (String) arr[0], (Long) arr[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<ImportantProjectSumFundsGroupByRankVO> countImportantProjectSumFundsGroupByRank(MonthlyScientificResearchReportQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Statistics> root = cq.from(Statistics.class);

        // 构建统计字段
        cq.multiselect(
                root.get("rank"),  // 分组字段
                cb.sum(root.get("projectfunds"))     // 统计数量
        );

        // 动态条件构建
        PredicateCallBack predicateCallBack = new PredicateCallBack() {
            @Override
            public List<Predicate> toPredicates(Root rt, CriteriaQuery query, CriteriaBuilder cb) {
                return Arrays.asList(cb.equal(root.get("projectImportant"),Integer.valueOf(Constants.ALL_YES)));
            }
        };

        Predicate predicate = buildSpecification(root,cq, cb, queryDTO,predicateCallBack);
        cq.where(predicate);

        // 分组配置
        cq.groupBy(root.get("rank"));

        // 执行数据库层面分组查询
        List<Object[]> results = entityManager.createQuery(cq).getResultList();
        // 结果转换
        return results.stream()
                .map(arr -> new ImportantProjectSumFundsGroupByRankVO(
                        (String) arr[0], (BigDecimal) arr[1]))
                .collect(Collectors.toList());
    }

    Predicate buildSpecification(Root<?> root, CriteriaQuery cq, CriteriaBuilder cb, MonthlyScientificResearchReportQueryDTO queryDTO, PredicateCallBack predicateCallBack) {
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

        // 数值类型精确匹配
        if (queryDTO.getProjectLevel() != null) {
            predicates.add(cb.equal(root.get("projectLevel"), queryDTO.getProjectLevel()));
        }
        if (predicateCallBack != null && !CollectionUtils.isEmpty(predicateCallBack.toPredicates(root, cq, cb))) {
            List<Predicate> predicateList = predicateCallBack.toPredicates(root, cq, cb);
            for (Predicate predicate : predicateList) {
                predicates.add(predicate);
            }
        }


        return cb.and(predicates.toArray(new Predicate[0]));
    }

    private List<Predicate> buildPredicates(Root<?> root, CriteriaBuilder cb, MonthlyScientificResearchReportQueryDTO queryDTO) {
        List<Predicate> predicates = Lists.newArrayList();
        addIfNotEmpty(predicates, root, cb, "principalUnit", queryDTO.getPrincipalUnit());
        addIfNotEmpty(predicates, root, cb, "businessSector", queryDTO.getBusinessSector());
        addIfNotEmpty(predicates, root, cb, "researchAttributes", queryDTO.getResearchAttribute());

        return predicates;
    }
}
