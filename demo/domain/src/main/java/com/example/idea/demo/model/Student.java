package com.example.idea.demo.model;

import java.util.Date;

public class Student {
    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", createtime=" + createtime +
                ", modifytime=" + modifytime +
                '}';
    }

    public Student(){

    }

    public Student(String id,String name,Short sex,Integer age){
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
    }

    private String id;

    private String name;

    private Integer age;

    private Short sex;

    private Date createtime;

    private Date modifytime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Short getSex() {
        return sex;
    }

    public void setSex(Short sex) {
        this.sex = sex;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }




}