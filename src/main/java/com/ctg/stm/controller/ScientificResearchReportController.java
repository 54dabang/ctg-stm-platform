package com.ctg.stm.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.ctg.stm.domain.Statistics;
import com.ctg.stm.dto.MonthlyScientificResearchReportQueryDTO;
import com.ctg.stm.util.BeanCopyUtil;
import com.ctg.stm.vo.*;
import com.ctg.stm.service.StatisticsService;
import com.ctg.stm.util.ProjectEnum;
import com.ctg.stm.util.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@Api(value = "", tags = {"集团科研情况首页"})
public class ScientificResearchReportController {


    @Autowired
    private StatisticsService statisticsService;

    /*@ApiOperation(value = "项目整体情况，基于查询全表实现")
    @RequestMapping(value = "/api/projectOverallSituation2", method = RequestMethod.POST)
    public Result projectOverallSituation2(@RequestBody MonthlyScientificResearchReportQueryDTO queryDTO) {

        List<Statistics> list = statisticsService.findByMonthlyScientificResearchReportQueryDTO(queryDTO, null);
        Map<Integer, Long> statusCountMap = list.stream()
                .filter(unit -> unit.getBpmStatus() != null)
                .collect(Collectors.groupingBy(
                        Statistics::getBpmStatus,
                        Collectors.counting()
                ));

        // 获取枚举所有状态描述列表
        List<Integer> allStatusDescList = ProjectEnum.ProBpmStatus.getValueList();

        // 补全缺失状态并设置默认值0
        Map<Integer, Long> result = allStatusDescList.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        bpmStatus -> statusCountMap.getOrDefault(bpmStatus, 0L)
                ));
        Map<String, Long> finalResult = result.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> ProjectEnum.ProBpmStatus.getByValue(entry.getKey()).desc(),
                        Map.Entry::getValue
                ));

        return Result.success(finalResult);
    }*/

    @ApiOperation(value = "项目整体情况")
    @RequestMapping(value = "/api/projectOverallSituation", method = RequestMethod.POST)
    public Result projectOverallSituation(@RequestBody MonthlyScientificResearchReportQueryDTO queryDTO) {
        List<ProjectCountGroupByBpmStatusVO> list = statisticsService.countProjectNumGroupByProBpmStatus(queryDTO);
        Map<Integer, Long> statusCountMap = new HashMap<>();
        list.forEach(item -> {
            statusCountMap.put(item.getProBpmStatus(), item.getCount());
        });
        Map<String, Long> finalResult = ProjectEnum.ProBpmStatus.getValueList().stream()
                .collect(Collectors.toMap(
                        status -> ProjectEnum.ProBpmStatus.getByValue(status).desc(),
                        status -> statusCountMap.getOrDefault(status, 0L)
                ));
        return Result.success(finalResult);
    }

    @ApiOperation(value = "在研项目数")
    @RequestMapping(value = "/api/projectProcessing", method = RequestMethod.POST)
    public Result projectProcessing(@RequestBody MonthlyScientificResearchReportQueryDTO queryDTO) {

        List<ProjectCountGroupByProjectCategoryVO> categoryGroupDTOList = statisticsService.countProjectNumGroupByProjectCategoryUnderDevelopment(queryDTO);
        Map<String, Long> result = ProjectEnum.ProjectCategory.getDescList().stream()
                .collect(Collectors.toMap(
                        projectCategory -> projectCategory,
                        projectCategory -> categoryGroupDTOList.stream()
                                .filter(item -> item.getProjectCategory().equals(projectCategory))
                                .findFirst()
                                .map(ProjectCountGroupByProjectCategoryVO::getCount)
                                .orElse(0L)
                ));

        return Result.success(result);
    }

    @ApiOperation(value = "科研成果数")
    @RequestMapping(value = "/api/projectResult", method = RequestMethod.POST)
    public Result projectResult(@RequestBody MonthlyScientificResearchReportQueryDTO queryDTO) {

        List<ProjectResultCountGroupByProjectCategoryVO> categoryGroupDTOList = statisticsService.countProjectResultGroupByProjectCategory(queryDTO);
        Map<String, Long> result = ProjectEnum.ProjectCategory.getDescList().stream()
                .collect(Collectors.toMap(
                        projectCategory -> projectCategory,
                        projectCategory -> categoryGroupDTOList.stream()
                                .filter(item -> item.getProjectCategory().equals(projectCategory))
                                .findFirst()
                                .map(ProjectResultCountGroupByProjectCategoryVO::getCount)
                                .orElse(0L)
                ));

        return Result.success(result);
    }


    @ApiOperation(value = "在研项目数")
    @RequestMapping(value = "/api/countProjectNumUnderDevelopment", method = RequestMethod.POST)
    public Result countProjectNumUnderDevelopment(@RequestBody MonthlyScientificResearchReportQueryDTO queryDTO) {

        Long result = statisticsService.countProjectNumUnderDevelopment(queryDTO);
        return Result.success(result);
    }

    @ApiOperation(value = "验收项目数")
    @RequestMapping(value = "/api/countProjectNumUnderAcceptance", method = RequestMethod.POST)
    public Result countProjectNumUnderAcceptance(@RequestBody MonthlyScientificResearchReportQueryDTO queryDTO) {

        Long result = statisticsService.countProjectNumUnderAcceptance(queryDTO);
        return Result.success(result);
    }


    @ApiOperation(value = "科研成果数")
    @RequestMapping(value = "/api/sumProjectResult", method = RequestMethod.POST)
    public Result sumProjectResult(@RequestBody MonthlyScientificResearchReportQueryDTO queryDTO) {
        Integer result = statisticsService.sumProjectResult(queryDTO);
        return Result.success(result);
    }


    @ApiOperation(value = "科研投入")
    @RequestMapping(value = "/api/sumProjectTotalFunds", method = RequestMethod.POST)
    public Result sumProjectTotalFunds(@RequestBody MonthlyScientificResearchReportQueryDTO queryDTO) {

        BigDecimal result = statisticsService.sumProjectTotalFunds(queryDTO);
        return Result.success(result);
    }


    @ApiOperation(value = "重点项目投入")
    @RequestMapping(value = "/api/importantProjectFunds", method = RequestMethod.POST)
    public Result importantProjectFunds(@RequestBody MonthlyScientificResearchReportQueryDTO queryDTO) {

        List<Statistics> statisticsList = statisticsService.findImportantProjectTotalFunds(queryDTO);
        return Result.success(statisticsList);
    }

    @ApiOperation(value = "保存项目基本统计单元")
    @RequestMapping(value = "/api/saveStatistics", method = RequestMethod.POST)
    public Result saveStatistics(@RequestBody Statistics statistics) {
        Statistics res;
        if (statistics.getId() != null) {
            Statistics target = statisticsService.findOne(statistics.getId());
            CopyOptions options = CopyOptions.create().setIgnoreNullValue(true);
            options.setIgnoreProperties("id");
            // 复制属性，忽略空值
            BeanUtil.copyProperties(statistics, target, options);
            res = statisticsService.save(target);
        } else {
            res = statisticsService.save(statistics);
        }

        return Result.success(res);
    }

    @ApiOperation(value = "验收项目数(各单位处于各种状态的项目数量)")
    @RequestMapping(value = "/api/countProjectNumGroupByPrincipalUnitAndProBpmStatus", method = RequestMethod.POST)
    public Result countProjectNumGroupByPrincipalUnitAndProBpmStatus(@RequestBody MonthlyScientificResearchReportQueryDTO queryDTO) {
        List<ProjectPrincipalUnitBpmStatusVO> projectPrincipalUnitBpmStatusVOList = statisticsService.countProjectNumGroupByPrincipalUnitAndProBpmStatus(queryDTO);
        return Result.success(projectPrincipalUnitBpmStatusVOList);
    }


    @ApiOperation(value = "各单位研发投入")
    @RequestMapping(value = "/api/countProjectTotalFundsGroupByPrincipalUnit", method = RequestMethod.POST)
    public Result countProjectTotalFundsGroupByPrincipalUnit(@RequestBody MonthlyScientificResearchReportQueryDTO queryDTO) {
        List<ProjectfundsGroupByPrincipalUnitVO> projectPrincipalUnitBpmStatusVOList = statisticsService.countProjectTotalFundsGroupByPrincipalUnit(queryDTO);
        return Result.success(projectPrincipalUnitBpmStatusVOList);
    }

    @ApiOperation(value = "获取所有项目")
    @RequestMapping(value = "/api/findAllProjectStatistics", method = RequestMethod.POST)
    public Result findAllProjectStatistics() {
        List<Statistics> statisticsList = statisticsService.findAllStatistics();
        List<StatisticsVO> statisticsVoList = statisticsList.stream()
                .map(s -> new StatisticsVO(s))
                .collect(Collectors.toList());
        return Result.success(statisticsVoList);
    }
}
