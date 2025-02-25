package com.ctg.stm.controller;

import com.alibaba.fastjson.JSONObject;
import com.ctg.stm.domain.Student;
import com.ctg.stm.dto.StudentDTO;
import com.ctg.stm.repository.StudentRepository;
import com.ctg.stm.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: leixingbang
 * @create: 2022/06/01 16:18
 * @description:
 */
@RestController
@CrossOrigin
public class CommonController {

    @Autowired
    private StudentRepository studentRepository;
    @RequestMapping("/findAll")
    public String getBusinessList(@RequestBody JSONObject submitJson, @RequestParam("size") Long size) {
        List<Student> studentList = studentRepository.findAll();
        return Constants.getResponseStr(Constants.CODE_SUC, "成功",studentList);
    }
    @RequestMapping("/api/findAllStudents")
    public String findAllStudents(){
        List<Student> studentList = studentRepository.findAll();
        return Constants.getResponseStr(Constants.CODE_SUC, "成功",studentList);
        //return Constants.getResponseStr(Constants.CODE_SUC,"成功",environmentConfig.getConfigs());
    }
    @RequestMapping("/api/saveStudent")
    public String saveStudent(@RequestBody StudentDTO studentDTO){
        Student student = new Student();
        //student.setId(studentDTO.getId());
        student.setName(studentDTO.getName());
        student.setEnrollmentDate(studentDTO.getEnrollmentDate());
        studentRepository.save(student);
        return Constants.getResponseStr(Constants.CODE_SUC,"成功",student);
    }




}
