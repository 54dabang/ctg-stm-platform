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
import java.util.Random;


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

        // 内部科研项目，从MA_PRJ_I_S_PROJECT_TD中查询项目数据
        String sql = "SELECT ID, PROJECT_NAME, PRINCIPAL_UNIT, PROJECT_PRINCIPAL, PROJECT_CATEGORY, PROJECT_TYPE, TOTAL_FUNDS, PROJECT_LEVEL, RESEARCH_ATTRIBUTES, BUSINESS_SECTOR, PROFESSIONAL, BPM_STATUS, PROJECT_CODE, LX_CODE FROM MA_PRJ_I_S_PROJECT_TD WHERE DEL = 0";
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
                        projectCategory = "A类国家级";
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
            String projectStatus; //项目状态
            String acceptancePoint = ""; //所处验收节点
            BigDecimal projectFunds = null; //研发投入
            if (bpmStatus == null) {
                projectStatus = "项目状态数据缺失";
            }else{
                try {
                    double bpm = Double.parseDouble(bpmStatus);
                    if(bpm < 39){
                        projectStatus = "立项阶段";
                    }else if(bpm > 39 && bpm < 79){
                        projectStatus = "验收阶段";
                        if(bpm==41.1){
                            acceptancePoint = "发起验收申请";
                        }else if (bpm==41.2){
                            acceptancePoint = "立项单位（部门）审核";
                        }else if (bpm==41.3){
                            acceptancePoint = "二级单位科技部门经办人审核";
                        }else if (bpm==41.4){
                            acceptancePoint = "二级单位科技部门审核";
                        }else if (bpm==41.5){
                            acceptancePoint = "集团科创部经办人审核";
                        }else if (bpm==41.52){
                            acceptancePoint = "专业部门经办人";
                        }else if (bpm==41.6){
                            acceptancePoint = "集团科创部内部审核";
                        }
                    }else{
                        projectStatus = "实施阶段";
//                        Random random = new Random();
//                        // 随机生成投入（测试用）
//
//                        assert totalFunds != null;
//                        if(totalFunds.compareTo(BigDecimal.ZERO)>0) {
//                            projectFunds = totalFunds.multiply(BigDecimal.valueOf(Math.random()));
//                        }

                    }
                } catch (NumberFormatException e) {
                    //bpm =999为流程作废，暂不能确定是处于立项阶段还是验收阶段
                    projectStatus = "暂无法确定";
                }
            }


            String projectCode = (String) columns[12];//项目编号
            String lxCode = (String) columns[13];//立项编号
            String contractFuns = "SELECT ACT_PAY_AMOUNT FROM STMS.STATISTICS_CONTRACT_PAYMENT WHERE CN_TECH_PROJECT_NO = ?1";
            String baoxiaoFuns = "SELECT VAT_AMOUNT FROM STMS.STATISTICS_REIMBURSEMENT_AMOUNT WHERE ZYX29 = ?1";
            Query contractFunsQuery = entityManager.createNativeQuery(contractFuns);
            List<BigDecimal> contractFundsResult = contractFunsQuery.setParameter(1, projectCode).getResultList();

            Query baoxiaoFunsQuery = entityManager.createNativeQuery(baoxiaoFuns);
            List<BigDecimal> baoxiaoFunsResult = baoxiaoFunsQuery.setParameter(1, lxCode).getResultList();

            BigDecimal contractSum = BigDecimal.ZERO;
            BigDecimal baoxiaoSum = BigDecimal.ZERO;

            if(!contractFundsResult.isEmpty()){
                for (BigDecimal num : contractFundsResult) {
                    contractSum = contractSum.add(num);
                }
            }

            if(!baoxiaoFunsResult.isEmpty()){
                for (BigDecimal num : baoxiaoFunsResult) {
                    baoxiaoSum = baoxiaoSum.add(num);
                }
            }

            projectFunds = contractSum.add(baoxiaoSum);
            System.out.println(projectFunds);

            //项目成果数
            int projectResult = 0;
            String countProjectResult = "SELECT COUNT(*) as PROJECT_RESULT FROM STMS.MA_PRJ_E_S_PROJECT_CGJL_TD WHERE PROJECT_ID = ?1 AND DEL = 0 GROUP BY PROJECT_ID HAVING COUNT(*) > 1";
            Query queryResult = entityManager.createNativeQuery(countProjectResult);
            queryResult.setParameter(1, projectID);
            if (!queryResult.getResultList().isEmpty()) {
                projectResult = Integer.parseInt(queryResult.getResultList().get(0).toString());
            }else{
                projectResult = 0;
            }
            //项目成果ID集合
            Integer resultZL=0;
            Integer resultRZ=0;
            Integer resultBZ=0;
            Integer resultLW=0;
            Integer resultZZ=0;
            String projectResultIDs = "SELECT STMS.MA_PRJ_E_S_PROJECT_CGJL_TD.KJJL_ID FROM STMS.MA_PRJ_E_S_PROJECT_CGJL_TD WHERE PROJECT_ID = ?1 AND DEL = 0";
            Query projectResultIDsQuery = entityManager.createNativeQuery(projectResultIDs);
            List<String> kjjlID = projectResultIDsQuery.setParameter(1, projectID).getResultList();
            if (!kjjlID.isEmpty()) {
                for (String summaryID : kjjlID) {
                    String resultType = "SELECT RESULT_TYPE FROM STMS.RESULT_SUMMARY WHERE ID = ?1 AND DEL = 0";
                    Query resultTypeQuery = entityManager.createNativeQuery(resultType);
                    List<String> type = resultTypeQuery.setParameter(1, summaryID).getResultList();
                    if(!type.isEmpty()){
                        resultType = type.get(0);
                        if(resultType.equals("01")){
                            resultZL = resultZL+1;
                        }else if(resultType.equals("02")){
                            resultRZ = resultRZ+1;
                        }else if(resultType.equals("03")){
                            resultBZ = resultBZ+1;
                        }else if(resultType.equals("04")){
                            resultLW = resultLW+1;
                        }else if(resultType.equals("05")){
                            resultZZ = resultZZ+1;
                        }
                    }
                }
               projectResultIDsQuery.getResultList();
            }

            String insertSql = "INSERT INTO STATISTICS (PROJECT_ID, PROJECT_NAME, PRINCIPAL_UNIT, PRINCIPAL_NAME, PROJECT_CATEGORY, PROJECT_TYPE, PROJECT_LEVEL, TOTAL_FUNDS, RESEARCH_ATTRIBUTES, BUSINESS_SECTOR, PROFESSIONAL, BPM_STATUS, PROJECT_STATUS, ACCEPTANCE_POINT, PROJECT_RESULT, PROJECT_FUNDS, RESULT_ZL, RESULT_LW, RESULT_BZ, RESULT_ZZ, RESULT_RZ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                    .setParameter(13, projectStatus)
                    .setParameter(14, acceptancePoint)
                    .setParameter(15, projectResult)
                    .setParameter(16, projectFunds)
                    .setParameter(17, resultZL)
                    .setParameter(18, resultLW)
                    .setParameter(19, resultBZ)
                    .setParameter(20, resultZZ)
                    .setParameter(21, resultRZ)
                    .executeUpdate();
        }

        //重点项目
        String projectImportant = "SELECT PROJECT_NAME, PROJECT_PRINCIPAL, PRINCIPAL_UNIT, START_TIME, END_TIME, GOAL_MILESTONE, PROJECT_CATEGORY, AMOUNT_PAYED_YEAR FROM STATISTICS_WBXM";
        Object[] projectImportantResult = entityManager.createNativeQuery(projectImportant).getResultList().toArray();
        for (Object row : projectImportantResult) {
            Object[] columns = (Object[]) row;
            String projectName = (String) columns[0];//项目名称
            String projectPrincipal = (String) columns[1];//项目填报人
            String principalUnit = (String) columns[2];//项目单位
            String startTime = columns[3].toString();
            String endTime = columns[4].toString();
            String milestone = columns[5].toString();//项目里程碑
            String projectCate = (String) columns[6];//项目类型
            BigDecimal amountPayed =  new BigDecimal(columns[7].toString());//年度完成金额

            String projectRank;
            if (projectCate.contains("部")) {
                projectCate = "省部级";
                projectRank = "省部级项目";
            } else if(projectCate.contains("怀柔")) {
                projectRank = "怀柔实验室";
                projectCate = "国家级";
            } else{
                projectRank = "国家级项目";
                projectCate = "国家级";
            }

            String insertSql2 = "INSERT INTO STATISTICS (PROJECT_NAME, PRINCIPAL_UNIT, PROJECT_CATEGORY, PROJECT_IMPORTANT, PROJECT_RANK, START_TIME, END_TIME, PRINCIPAL_NAME, PROJECT_MILESTONE, PROJECT_FUNDS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            entityManager.createNativeQuery(insertSql2)
                    .setParameter(1, projectName)
                    .setParameter(2, principalUnit)
                    .setParameter(3, projectCate)
                    .setParameter(4, 1)
                    .setParameter(5, projectRank)
                    .setParameter(6, startTime)
                    .setParameter(7, endTime)
                    .setParameter(8, projectPrincipal)
                    .setParameter(9, milestone)
                    .setParameter(10, amountPayed)
                    .executeUpdate();
        }

    }

    @Transactional
    @Override
    public void syncRewardData() {
        String deleteSql = "DELETE FROM STATISTICS_REWARDS";
        entityManager.createNativeQuery(deleteSql).executeUpdate();
        String sql = "SELECT ID, QR_REWARDS_CLASS FROM MA_WBCGJL_DECLARATION_TD";
        Object[] result = entityManager.createNativeQuery(sql).getResultList().toArray();
        for (Object row : result) {
            Object[] columns = (Object[]) row;
            String rewardID = (String) columns[0];//奖励ID
            String rewardClass = (String) columns[1];//奖励级别
            if(rewardID != null && rewardClass != null){
                if(rewardClass.equals("0")){
                    rewardClass = "国家级";
                }else if(rewardClass.equals("1")){
                    rewardClass = "省部级";
                }else if(rewardClass.equals("2")){
                    rewardClass = "行业学会级";
                }else{
                    rewardClass = "其它";
                }
            }else{
                rewardClass = "其它";
            }
            String insertSql = "INSERT INTO STATISTICS_REWARDS (REWARD_ID, REWARD_CLASS) VALUES (?, ?)";
            entityManager.createNativeQuery(insertSql)
                    .setParameter(1, rewardID)
                    .setParameter(2, rewardClass)
                    .executeUpdate();
        }
    }
}
