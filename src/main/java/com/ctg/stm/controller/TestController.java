package com.ctg.stm.controller;


import com.ctg.stm.domain.Student;
import com.ctg.stm.repository.StudentRepository;
import com.ctg.stm.service.impl.StatisticsSchedulerServiceImpl;
import com.ctg.stm.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private StatisticsSchedulerServiceImpl statisticsSchedulerService;
    @RequestMapping(value = "/copy", method = RequestMethod.GET)
    public void copy(){
        statisticsSchedulerService.syncProjectData();
    }

}
