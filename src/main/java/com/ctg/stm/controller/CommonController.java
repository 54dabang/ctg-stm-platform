package com.ctg.stm.controller;

import com.alibaba.fastjson.JSONObject;
import com.ctg.stm.domain.Student;
import com.ctg.stm.dto.StudentDTO;
import com.ctg.stm.repository.StudentRepository;
import com.ctg.stm.util.Constants;
import com.ctg.stm.util.Result;
import io.swagger.annotations.ApiOperation;
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

    @RequestMapping("/api/findAllStudents")
    public Result<List<Student>> findAllStudents(){
        List<Student> studentList = studentRepository.findAll();
        return Result.success(studentList);
    }
    @RequestMapping("/api/saveStudent")
    @ApiOperation(value = "用户基本信息", notes = "保存用户信息")
    public Result<Student> saveStudent(@RequestBody StudentDTO studentDTO){
        Student student = new Student();
        //student.setId(studentDTO.getId());
        student.setName(studentDTO.getName());
        student.setEnrollmentDate(studentDTO.getEnrollmentDate());
        student = studentRepository.save(student);
        return Result.success(student);
    }




}
