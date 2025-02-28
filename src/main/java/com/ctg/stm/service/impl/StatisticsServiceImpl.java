package com.ctg.stm.service.impl;

import com.ctg.stm.domain.Statistics;
import com.ctg.stm.dto.MonthlyScientificResearchReportQueryDTO;
import com.ctg.stm.dto.ProjectQueryDetailDTO;
import com.ctg.stm.dto.StatisticsSearchDTO;
import com.ctg.stm.util.Constants;
import com.ctg.stm.vo.*;
import com.ctg.stm.repository.StatisticsRepository;
import com.ctg.stm.service.PredicateCallBack;
import com.ctg.stm.service.StatisticsService;
import com.ctg.stm.util.ProjectEnum;
import com.google.common.collect.Lists;
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
    public Statistics findOne(Long id) {
        return statisticsRepository.findById(id).get();
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
        Predicate inPredicate = root.get("projectStatus")
                .in(ProjectEnum.ProjectStatus.PROCESSING.desc());
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
        Predicate inPredicate = root.get("projectStatus")
                .in(ProjectEnum.ProjectStatus.ACCEPTANCE.desc());
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
    public List<ProjectPrincipalUnitProjectStatusVO> countProjectNumGroupByPrincipalUnitAndProBpmStatus(MonthlyScientificResearchReportQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Statistics> root = cq.from(Statistics.class);

        // 构建统计字段（分组字段 + 计数）
        cq.multiselect(
                root.get("principalUnit"),    // 分组字段2
                root.get("projectStatus"),        // 分组字段1
                cb.count(root.get("id"))      // 统计数量
        );

        // 动态条件构建（保留原有过滤逻辑）
        List<Predicate> predicates = buildPredicates(root, cb, queryDTO);

        cq.where(predicates.toArray(new Predicate[0]));

        // 分组配置（双字段分组）
        cq.groupBy(
                root.get("projectStatus"),
                root.get("principalUnit")
        );

        // 添加排序（可选）
        cq.orderBy(
                cb.asc(root.get("projectStatus")),
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
                cb.sum(root.get("projectfunds"))      // 累加项目投入
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
                .map(r -> new ProjectfundsGroupByPrincipalUnitVO((String) r[0], Optional.ofNullable(r[1]).map(BigDecimal.class::cast).orElse(BigDecimal.ZERO)))
                .collect(Collectors.toList());
    }

    // 结果转换方法
    private ProjectPrincipalUnitProjectStatusVO convertToVO(Object[] result) {
        return new ProjectPrincipalUnitProjectStatusVO(
                (String) result[0],  //
                (String) result[1],
                (Long) result[2]     // count
        );
    }


    @Override
    public List<Statistics> findProjectUnderDevelopment(MonthlyScientificResearchReportQueryDTO queryDTO) {
        PredicateCallBack predicateCallBack = (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();

            Predicate inPredicate = root.get("projectStatus")
                    .in(ProjectEnum.ProjectStatus.PROCESSING.desc());
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
        if (Objects.nonNull(value) && !StringUtils.isEmpty(value)) {
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
        Predicate inPredicate = root.get("projectStatus")
                .in(ProjectEnum.ProjectStatus.PROCESSING.desc());
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
    public List<ProjectCountGroupByProjectStatusVO> countProjectNumGroupByProBpmStatus(MonthlyScientificResearchReportQueryDTO queryDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Statistics> root = cq.from(Statistics.class);

        // 构建统计字段
        cq.multiselect(
                root.get("projectStatus"),  // 分组字段
                cb.count(root.get("id"))     // 统计数量
        );

        // 动态条件构建
        List<Predicate> predicates = buildPredicates(root, cb, queryDTO);
        cq.where(predicates.toArray(new Predicate[0]));

        // 分组配置
        cq.groupBy(root.get("projectStatus"));

        // 执行数据库层面分组查询
        List<Object[]> results = entityManager.createQuery(cq).getResultList();
        // 结果转换
        return results.stream()
                .map(arr -> new ProjectCountGroupByProjectStatusVO(
                        (String) arr[0], (Long) arr[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<Statistics> findImportantProjectTotalFunds(MonthlyScientificResearchReportQueryDTO queryDTO) {


        Specification<Statistics> spec = (root, query, cb) -> {
            PredicateCallBack predicateCallBack = new PredicateCallBack() {
                @Override
                public List<Predicate> toPredicates(Root rt, CriteriaQuery query, CriteriaBuilder cb) {
                    return Arrays.asList(cb.equal(root.get("projectImportant"), Integer.valueOf(Constants.ALL_YES)));
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
    public List<ImportantProjectCountGroupByRankVO> countImportantProjectNumGroupByRank() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Statistics> root = cq.from(Statistics.class);

        // 构建统计字段
        cq.multiselect(
                root.get("projectRank"),  // 分组字段
                cb.count(root.get("id"))     // 统计数量
        );

        // 动态条件构建
        PredicateCallBack predicateCallBack = new PredicateCallBack() {
            @Override
            public List<Predicate> toPredicates(Root rt, CriteriaQuery query, CriteriaBuilder cb) {
                return Arrays.asList(cb.equal(root.get("projectImportant"), Integer.valueOf(Constants.ALL_YES)));
            }
        };
        MonthlyScientificResearchReportQueryDTO queryDTO = new MonthlyScientificResearchReportQueryDTO();

        Predicate predicate = buildSpecification(root, cq, cb, queryDTO, predicateCallBack);
        cq.where(predicate);

        // 分组配置
        cq.groupBy(root.get("projectRank"));

        // 执行数据库层面分组查询
        List<Object[]> results = entityManager.createQuery(cq).getResultList();
        // 结果转换
        return results.stream()
                .map(arr -> new ImportantProjectCountGroupByRankVO(
                        (String) arr[0], (Long) arr[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<ImportantProjectSumFundsGroupByRankVO> countImportantProjectSumFundsGroupByRank() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Statistics> root = cq.from(Statistics.class);

        // 构建统计字段
        cq.multiselect(
                root.get("projectRank"),  // 分组字段
                cb.sum(root.get("projectfunds"))     // 统计数量
        );

        // 动态条件构建
        PredicateCallBack predicateCallBack = new PredicateCallBack() {
            @Override
            public List<Predicate> toPredicates(Root rt, CriteriaQuery query, CriteriaBuilder cb) {
                return Arrays.asList(cb.equal(root.get("projectImportant"), Integer.valueOf(Constants.ALL_YES)));
            }
        };

        MonthlyScientificResearchReportQueryDTO queryDTO = new MonthlyScientificResearchReportQueryDTO();

        Predicate predicate = buildSpecification(root, cq, cb, queryDTO, predicateCallBack);
        cq.where(predicate);

        // 分组配置
        cq.groupBy(root.get("projectRank"));

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
        addIfNotEmpty(predicates, root, cb, "researchAttributes", queryDTO.getResearchAttributes());

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
        addIfNotEmpty(predicates, root, cb, "researchAttributes", queryDTO.getResearchAttributes());

        return predicates;
    }

    @Override
    public Page<Statistics> findAll(Pageable pageable, Specification specification) {
        // 执行分页查询
        Page<Statistics> page = statisticsRepository.findAll(specification, pageable);

        // 封装返回结果
        PageVO<Statistics> result = new PageVO<>();
        result.setTotal(page.getTotalElements());
        result.setPages(page.getTotalPages());
        result.setCurrent(page.getNumber()); // 当前页码（从0开始）
        result.setSize(page.getSize());
        result.setRecords(page.getContent());

        return page;
    }

    @Override
    public Page<Statistics> importantProjectPage(StatisticsSearchDTO searchDTO) {
        // 构造Pageable对象
        Pageable pageable = buildPage(searchDTO);

        Specification<Statistics> spec = null;
        if (!StringUtils.isEmpty(searchDTO.getKeywords())) {
            // 构造动态筛选条件（假设关键词匹配多个字段）
            spec = (root, query, criteriaBuilder) -> {
                // 关键字匹配多个字段
                return criteriaBuilder.and(
                        criteriaBuilder.like(root.get("projectName"), "%" + searchDTO.getKeywords() + "%")
                );
            };
        }

        return this.findAll(pageable, spec);
    }


    private Pageable buildPage(StatisticsSearchDTO searchDTO) {
        Sort sort = Sort.by(searchDTO.getOrder().equalsIgnoreCase("asc") ? Sort.Order.asc(searchDTO.getSort()) : Sort.Order.desc(searchDTO.getSort()));
        return PageRequest.of(searchDTO.getCurrent(), searchDTO.getSize(), sort);
    }

    @Override
    public Page<Statistics> statisticspage(ProjectQueryDetailDTO projectQueryDetailDTO) {
        Pageable pageable = buildPage(projectQueryDetailDTO.getSearchDTO());
        Specification<Statistics> spec = (root, query, cb) -> {
            PredicateCallBack predicateCallBack = new PredicateCallBack() {
                @Override
                public List<Predicate> toPredicates(Root rt, CriteriaQuery query, CriteriaBuilder cb) {
                    String keyword = Optional.ofNullable(projectQueryDetailDTO.getSearchDTO().getKeywords()).orElse("");
                    Predicate predicate = cb.like(root.get("projectName"), "%" + keyword + "%");
                    return Arrays.asList(predicate);
                }
            };
            return buildSpecification(root, query, cb, projectQueryDetailDTO.getMonthlyScientificResearchReportQueryDTO(), predicateCallBack);
        };
        return this.findAll(pageable, spec);
    }
}
