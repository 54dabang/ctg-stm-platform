package com.ctg.stm.controller;

import com.ctg.stm.domain.Statistics;
import com.ctg.stm.dto.MonthlyScientificResearchReportQueryDTO;
import com.ctg.stm.dto.ProjectQueryDetailDTO;
import com.ctg.stm.dto.StatisticsSearchDTO;
import com.ctg.stm.service.StatisticsService;
import com.ctg.stm.util.Result;
import com.ctg.stm.vo.ImportantProjectCountGroupByRankVO;
import com.ctg.stm.vo.ImportantProjectSumFundsGroupByRankVO;
import com.ctg.stm.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin
@Api(value = "", tags = {"重点项目概览"})
public class ImportantProjectOverviewController {
    @Autowired
    private StatisticsService statisticsService;

    @ApiOperation(value = "重点项目投入")
    @RequestMapping(value = "/api/countImportantProjectSumFundsGroupByRank", method = RequestMethod.POST)
    public Result countImportantProjectSumFundsGroupByRank() {

        List<ImportantProjectSumFundsGroupByRankVO> importantProjectCountGroupByRankVOList = statisticsService.countImportantProjectSumFundsGroupByRank();
        return Result.success(importantProjectCountGroupByRankVOList);
    }
    @ApiOperation(value = "按照项目级别统计（单位 个）")
    @RequestMapping(value = "/api/countImportantProjectNumGroupByRank", method = RequestMethod.POST)
    public Result countImportantProjectNumGroupByRank() {

        List<ImportantProjectCountGroupByRankVO> importantProjectSumFundsGroupByRankVOList = statisticsService.countImportantProjectNumGroupByRank();

        return Result.success(importantProjectSumFundsGroupByRankVOList);
    }

    @PostMapping("/api/importantStatisticspage")
    @ApiOperation(value = "重点项目概览")
    public Result importantStatisticspage(@RequestBody StatisticsSearchDTO searchDTO) {
        return Result.success(statisticsService.importantProjectPage(searchDTO));
    }


    @PostMapping("/api/statisticspage")
    @ApiOperation(value = "验收项目清单")
    public Result statisticspage(@RequestBody ProjectQueryDetailDTO projectQueryDetailDTO) {
        return Result.success(statisticsService.statisticspage(projectQueryDetailDTO));
    }
}
