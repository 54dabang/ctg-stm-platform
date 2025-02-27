package com.ctg.stm.service.impl;

import com.ctg.stm.service.StatisticsSchedulerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service
public class StatisticsSchedulerServiceImpl implements StatisticsSchedulerService {
    @PersistenceContext
    private EntityManager entityManager;

    //    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    @Override
    public void syncProjectData() {
        // 清空统计表数据
        String deleteSql = "DELETE FROM STATISTICS";
        entityManager.createNativeQuery(deleteSql).executeUpdate();

        // 从MA_PRJ_I_S_PROJECT_TD中查询项目数据
        String sql = "SELECT ID, PROJECT_NAME, PRINCIPAL_UNIT, PROJECT_PRINCIPAL, PROJECT_CATEGORY, PROJECT_TYPE, TOTAL_FUNDS, PROJECT_LEVEL, RESEARCH_ATTRIBUTES, BUSINESS_SECTOR, PROFESSIONAL, BPM_STATUS FROM MA_PRJ_I_S_PROJECT_TD";
        Object[] result = entityManager.createNativeQuery(sql).getResultList().toArray();
        for (Object row : result) {
            Object[] columns = (Object[]) row;
            String projectID = (String) columns[0];//项目ID
            String projectName = (String) columns[1];//项目名称
            String principalUnit = (String) columns[2];//责任单位
            String selectDepartSql = "SELECT DEPART_NAME FROM SYS_DEPART WHERE ID = ?1";
            Query query = entityManager.createNativeQuery(selectDepartSql);
            query.setParameter(1, principalUnit);
            if (!query.getResultList().isEmpty()) {
                principalUnit = query.getResultList().get(0).toString();
            }else{
                principalUnit = "责任单位数据错误";
            }

            String projectPrincipal = (String) columns[3];//项目负责人

            String projectCategory = (String) columns[4];//项目层级（国家级、省部级、集团级、子企业级）
            if (projectCategory == null) {
                projectCategory = "类型数据缺失";
            }else{
                switch (projectCategory) {
                    case "A":
                        projectCategory = "A类国家/省部级";
                        break;
                    case "B":
                        projectCategory = "B类集团级";
                        break;
                    case "C":
                        projectCategory = "C类子企业级";
                        break;
                    case "D":
                        projectCategory = "D类子企业级";
                        break;
                    case "1":
                        projectCategory = "暂时无法确定是B类或C类";
                        break;
                    default:
                        projectCategory = "类型数据缺失";
                }
            }

            String projectType = (String) columns[5];//项目类别（自主、外联）
            if(projectCategory.equals("D类子企业级")){
                projectType = "自主科研项目";
            }
            if (projectType == null) {
                projectCategory = "类型数据缺失";
            }else{
                List<String> types = Arrays.asList("2", "3", "5");
                if (types.contains(projectType)) {
                    projectType = "自主科研项目";
                } else {
                    if (projectType.equals("1")) {
                        projectType = "外联科研项目";
                    } else {
                        projectType = "类型数据缺失";
                    }
                }
            }

            BigDecimal totalFunds = (BigDecimal) columns[6];//项目金额
            String projectLevel;//项目级别，依据totalFunds划分为大型、中型、小型
            if (totalFunds == null) {
                projectLevel = "项目金额数据缺失";
            }else{
                if (totalFunds.compareTo(BigDecimal.valueOf(50)) <= 0) {
                    projectLevel = "小型";
                } else if (totalFunds.compareTo(BigDecimal.valueOf(50)) > 0 && totalFunds.compareTo(BigDecimal.valueOf(150)) <= 0) {
                    projectLevel = "中型";
                } else if (totalFunds.compareTo(BigDecimal.valueOf(150)) > 0 && totalFunds.compareTo(BigDecimal.valueOf(3000)) <= 0) {
                    projectLevel = "大型";
                } else {
                    projectLevel = "重大";
                }
            }

            String researchAttributes = (String) columns[8];//"研发属性（基础研究、应用基础研究、应用研究、技术与产品开发（试验发展、软科学研究）、其它"
            if (researchAttributes == null) {
                researchAttributes = "类型数据缺失";
            }else{
                switch (researchAttributes) {
                    case "1":
                        researchAttributes = "基础研究";
                        break;
                    case "2":
                        researchAttributes = "应用基础研究";
                        break;
                    case "3":
                        researchAttributes = "技术与产品开发（试验发展）";
                        break;
                    case "6":
                        researchAttributes = "应用研究";
                        break;
                    case "5":
                        researchAttributes = "软科学研究";
                        break;
                    default:
                        researchAttributes = "类型数据缺失";
                }
            }

            String businessSector = (String) columns[9];//项目所属领域 对应业务板块（水电、新能源、火电）
            String professional = (String) columns[10];//专业
            if (businessSector == null) {
                businessSector = "领域数据缺失";
            }else{
                switch (businessSector) {
                    case "0":
                        businessSector = "水电";
                        if (professional == null) {
                            professional = "专业数据缺失";
                        }else{
                            switch (professional) {
                                case "1":
                                    professional = "工程建设";
                                    break;
                                case "2":
                                    professional = "机电建设";
                                    break;
                                case "3":
                                    professional = "机电运行";
                                    break;
                                case "4":
                                    professional = "安全监测";
                                    break;
                                case "5":
                                    professional = "调度运行";
                                    break;
                                case "6":
                                    professional = "环境保护";
                                    break;
                                case "7":
                                    professional = "抽水蓄能";
                                    break;
                                case "8":
                                    professional = "信息化";
                                    break;
                                default:
                                    professional = "专业数据错误";
                            }
                        }
                        break;
                    case "1":
                        businessSector = "新能源";
                        if (professional == null) {
                            professional = "专业数据缺失";
                        }else {
                            switch (professional) {
                                case "11":
                                    professional = "海上风电";
                                    break;
                                case "12":
                                    professional = "陆上风电";
                                    break;
                                case "13":
                                    professional = "光伏发电（分布式）";
                                    break;
                                case "14":
                                    professional = "光伏发电（集中式）";
                                    break;
                                default:
                                    professional = "专业数据错误";
                            }
                        }
                        break;
                    case "2":
                        businessSector = "火电";
                        if (professional == null) {
                            professional = "专业数据缺失";
                        }else{
                            if (professional.equals("21")) {
                                    professional = "火电";
                            }else{
                                professional = "专业数据错误";
                            }
                        }
                        break;
                    case "3":
                        businessSector = "生态环保";
                        if (professional == null) {
                            professional = "专业数据缺失";
                        }else{
                            switch (professional) {
                                case "31":
                                    professional = "生态环保";
                                    break;
                                case "32":
                                    professional = "水体治理";
                                    break;
                                case "33":
                                    professional = "污泥处理";
                                    break;
                                case "34":
                                    professional = "污染治理";
                                    break;
                                case "5":
                                    professional = "城市治理";
                                    break;
                                case "6":
                                    professional = "城市管网";
                                    break;
                                case "7":
                                    professional = "信息化";
                                    break;
                                default:
                                    professional = "专业数据错误";
                            }
                        }
                        break;
                    case "4":
                        businessSector = "新兴业务";
                        if (professional == null) {
                            professional = "专业数据缺失";
                        }else{
                            switch (professional) {
                                case "41":
                                    professional = "储能";
                                    break;
                                case "42":
                                    professional = "氢能";
                                    break;
                                case "43":
                                    professional = "光热";
                                    break;
                                case "44":
                                    professional = "地热";
                                    break;
                                case "45":
                                    professional = "海洋能";
                                    break;
                                case "46":
                                    professional = "综合能源";
                                    break;
                                case "47":
                                    professional = "生物质能";
                                    break;
                                case "48":
                                    professional = "核能";
                                    break;
                                case "49":
                                    professional = "其它";
                                    break;
                                default:
                                    professional = "专业数据错误";
                            }
                        }
                        break;
                    case "5":
                        businessSector = "国际业务";
                        if (professional == null) {
                            professional = "专业数据缺失";
                        }else{
                            switch (professional) {
                                case "51":
                                    professional = "水电";
                                    break;
                                case "52":
                                    professional = "新能源（海上风电）";
                                    break;
                                case "53":
                                    professional = "新能源（陆上风电）";
                                    break;
                                case "54":
                                    professional = "新能源（太阳能）";
                                    break;
                                case "55":
                                    professional = "新能源（储能）";
                                    break;
                                case "56":
                                    professional = "新能源（氢能）";
                                    break;
                                case "57":
                                    professional = "其他新兴业务";
                                    break;
                                default:
                                    professional = "专业数据错误";
                            }
                        }
                        break;
                    case "6":
                        businessSector = "其他";
                        if (professional == null) {
                            professional = "专业数据缺失";
                        }else{
                            switch (professional) {
                                case "61":
                                    professional = "信息化";
                                    break;
                                case "62":
                                    professional = "机电建设";
                                    break;
                                case "63":
                                    professional = "天然气";
                                    break;
                                case "4":
                                    professional = "其它";
                                    break;
                                default:
                                    professional = "专业数据错误";
                            }
                        }
                        break;
                    default:
                        businessSector = "类型数据缺失";
                }
            }



            String bpmStatus = (String) columns[11];//流程状态

            String insertSql = "INSERT INTO STATISTICS (PROJECT_ID, PROJECT_NAME, PRINCIPAL_UNIT, PRINCIPAL_NAME, PROJECT_CATEGORY, PROJECT_TYPE, PROJECT_LEVEL, TOTAL_FUNDS, RESEARCH_ATTRIBUTES, BUSINESS_SECTOR, PROFESSIONAL, BPM_STATUS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            entityManager.createNativeQuery(insertSql)
                    .setParameter(1, projectID)
                    .setParameter(2, projectName)
                    .setParameter(3, principalUnit)
                    .setParameter(4, projectPrincipal)
                    .setParameter(5, projectCategory)
                    .setParameter(6, projectType)
                    .setParameter(7, projectLevel)
                    .setParameter(8, totalFunds)
                    .setParameter(9, researchAttributes)
                    .setParameter(10, businessSector)
                    .setParameter(11, professional)
                    .setParameter(12, bpmStatus)
                    .executeUpdate();
        }

    }

    @Override
    public void statisticsScheduler() {
        String querySql = "SELECT * FROM MA_PRJ_I_S_PROJECT_TD";
        List<Map<String, Object>> resultList = entityManager.createNativeQuery(querySql)
                .getResultList();
        System.out.println(resultList.toArray());
    }

}
