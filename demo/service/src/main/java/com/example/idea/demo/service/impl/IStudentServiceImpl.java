package com.example.idea.demo.service.impl;

import com.example.idea.demo.dao.StudentMapper;
import com.example.idea.demo.model.Student;
import com.example.idea.demo.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class IStudentServiceImpl implements IStudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Student findStudent(String id) {
        return studentMapper.selectByPrimaryKey(id);
    }

    @Override
    public int deleteStudent(String id) {
        return studentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int modifyStudent(Student student) {
        student.setModifytime(new Date());
        return studentMapper.updateByPrimaryKey(student);
    }

    @Override
    public int insertStudent(Student student) {
        student.setCreatetime(new Date());
        return studentMapper.insert(student);
    }

    @Override
    public ArrayList<Student> getAllSutdent() {
        return studentMapper.getAllSutdent();
    }
}
