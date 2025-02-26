package com.ctg.stm.controller;

import com.ctg.stm.domain.ProjectStatisticsBasicUnit;
import com.ctg.stm.dto.MonthlyScientificResearchReportQueryDTO;
import com.ctg.stm.service.ProjectStatisticsBasicUnitService;
import com.ctg.stm.util.ProjectEnum;
import com.ctg.stm.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@Api(value = "", tags = {"集团科研情况首页"})
public class MonthlyScientificResearchReportController {

    @Autowired
    private ProjectStatisticsBasicUnitService basicUnitService;

    @ApiOperation(value = "项目整体情况")
    @RequestMapping(value = "/api/projectOverallSituation", method = RequestMethod.POST)
    public Result projectOverallSituation(@RequestBody MonthlyScientificResearchReportQueryDTO queryDTO) {

        List<ProjectStatisticsBasicUnit> list = basicUnitService.findByMonthlyScientificResearchReportQueryDTO(queryDTO, null);
        Map<String, Long> statusCountMap = list.stream()
                .filter(unit -> unit.getBpmStatus() != null)
                .collect(Collectors.groupingBy(
                        ProjectStatisticsBasicUnit::getBpmStatus,
                        Collectors.counting()
                ));

       // 获取枚举所有状态描述列表
        List<String> allStatusDescList = ProjectEnum.ProBpmStatus.getDescList();

       // 补全缺失状态并设置默认值0
        Map<String, Long> result = allStatusDescList.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        desc -> statusCountMap.getOrDefault(desc, 0L)
                ));

        return Result.success(result);
    }

    @ApiOperation(value = "在研项目数")
    @RequestMapping(value = "/api/projectProcessing", method = RequestMethod.POST)
    public Result projectProcessing(@RequestBody MonthlyScientificResearchReportQueryDTO queryDTO) {

        List<ProjectStatisticsBasicUnit> list = basicUnitService.findProjectUnderDevelopment(queryDTO);
        Map<String, Long> statusCountMap = list.stream()
                .filter(unit -> unit.getProjectCategory() != null)
                .collect(Collectors.groupingBy(
                        ProjectStatisticsBasicUnit::getProjectCategory,
                        Collectors.counting()
                ));

        // 获取枚举所有状态描述列表
        List<String> allStatusDescList = ProjectEnum.ProjectCategory.getDescList();

        // 补全缺失状态并设置默认值0
        Map<String, Long> result = allStatusDescList.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        desc -> statusCountMap.getOrDefault(desc, 0L)
                ));

        return Result.success(result);
    }

    @ApiOperation(value = "保存项目基本统计单元")
    @RequestMapping(value = "/api/saveProjectStatisticsBasicUnit", method = RequestMethod.POST)
    public Result saveProjectStatisticsBasicUnit(@RequestBody ProjectStatisticsBasicUnit statisticsBasicUnit) {
        ProjectStatisticsBasicUnit res = basicUnitService.save(statisticsBasicUnit);
        return Result.success(res);
    }
    @ApiOperation(value = "获取所有项目")
    @RequestMapping(value = "/api/findAllProjectStatisticsBasicUnit", method = RequestMethod.POST)
    public Result findAllProjectStatisticsBasicUnit(@RequestBody ProjectStatisticsBasicUnit statisticsBasicUnit) {
        List<ProjectStatisticsBasicUnit> res = basicUnitService.findAllProjectStatisticsBasicUnits();
        return Result.success(res);
    }
}
