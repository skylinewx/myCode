package com.example.idea.demo.service;

import com.example.idea.demo.model.Student;

import java.util.ArrayList;


public interface IStudentService {
    public Student findStudent(String id);
    public int deleteStudent(String id);
    public int modifyStudent(Student student);
    public int insertStudent(Student student);
    public ArrayList<Student> getAllSutdent();
}
