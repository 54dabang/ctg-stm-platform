package com.ctg.stm.controller;

import com.ctg.stm.domain.Student;
import com.ctg.stm.dto.MonthlyScientificResearchReportQueryDTO;
import com.ctg.stm.dto.StudentDTO;
import com.ctg.stm.util.Constants;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class MonthlyScientificResearchReportController {

    @ApiOperation(value = "Get user by ID", notes = "Get the user information by user ID")
    @RequestMapping(value = "/api/projectOverallSituation", method = RequestMethod.POST)
    public String projectOverallSituation(@RequestBody MonthlyScientificResearchReportQueryDTO queryDTO){

        return Constants.getResponseStr(Constants.CODE_SUC,"成功");
    }

}
