package com.example.idea.demo.controller;

import com.example.idea.demo.model.Student;
import com.example.idea.demo.service.IStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class StudentController {

    Logger logger = LoggerFactory.getLogger("StudentController");
    
    private static final String CATACH_NAME = "STUDENT";

    @Autowired
    private  IStudentService studentService;

    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/student/getAll")
    public ArrayList<Student> getAllStudent(){
        logger.info("getAllStudent");
        return studentService.getAllSutdent();
    }

    @GetMapping("/student/{id}")
    @Cacheable(value = CATACH_NAME,key = "#id")
    public Student findStudentById(@PathVariable("id") String id){
        logger.info("findStudentById，id:"+id);
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  studentService.findStudent(id);
    }

    @PostMapping("/student")
    public Student insertStudent(@RequestParam("id") String id, @RequestParam("name")String name,
                                 @RequestParam("sex")Short sex, @RequestParam("age")Integer age){
        logger.info("insertStudent，id:"+id+",name:"+name+",sex:"+sex+",age:"+age);
        try {
            Student student = new Student(id, name, sex, age);
            studentService.insertStudent(student);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  studentService.findStudent(id);
    }

    @PutMapping("/student")
    @CachePut(value = CATACH_NAME,key = "#id")
    public  Student modifyStudent(@RequestParam("id") String id, @RequestParam("name")String name,
                                  @RequestParam("sex")Short sex, @RequestParam("age")Integer age){
        logger.info("modifyStudent，id:"+id+",name:"+name+",sex:"+sex+",age:"+age);
        Student student = studentService.findStudent(id);
        if (student==null) {
            return new Student();
        }
        student.setAge(age);
        student.setName(name);
        student.setSex(sex);
        studentService.modifyStudent(student);
        return  student;
    }

    @DeleteMapping("/student")
    @CacheEvict(value = CATACH_NAME,key = "#id")
    public  String deleteStudent(@RequestParam("id") String id){
        logger.info("deleteStudent，id:"+id);
        int i = studentService.deleteStudent(id);
        if(i==1){
            return "success";
        }else
            return "field,no such student,id="+id+"";
    }
}
