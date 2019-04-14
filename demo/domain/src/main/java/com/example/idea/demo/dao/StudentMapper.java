package com.example.idea.demo.dao;

import com.example.idea.demo.model.Student;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.ArrayList;

@Mapper
public interface StudentMapper {
    @Delete({
        "delete from student",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String id);

    @Insert({
        "insert into student (id, name, ",
        "age, sex, createTime, ",
        "modifyTime)",
        "values (#{id,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, ",
        "#{age,jdbcType=INTEGER}, #{sex,jdbcType=DECIMAL}, #{createtime,jdbcType=TIMESTAMP}, ",
        "#{modifytime,jdbcType=TIMESTAMP})"
    })
    int insert(Student record);

    @InsertProvider(type=StudentSqlProvider.class, method="insertSelective")
    int insertSelective(Student record);

    @Select({
        "select",
        "id, name, age, sex, createTime, modifyTime",
        "from student",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="age", property="age", jdbcType=JdbcType.INTEGER),
        @Result(column="sex", property="sex", jdbcType=JdbcType.DECIMAL),
        @Result(column="createTime", property="createtime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="modifyTime", property="modifytime", jdbcType=JdbcType.TIMESTAMP)
    })
    Student selectByPrimaryKey(String id);

    @UpdateProvider(type=StudentSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Student record);

    @Update({
        "update student",
        "set name = #{name,jdbcType=VARCHAR},",
          "age = #{age,jdbcType=INTEGER},",
          "sex = #{sex,jdbcType=DECIMAL},",
          "createTime = #{createtime,jdbcType=TIMESTAMP},",
          "modifyTime = #{modifytime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(Student record);

    @Select({
            "select",
            "id, name, age, sex, createTime, modifyTime",
            "from student"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="age", property="age", jdbcType=JdbcType.INTEGER),
            @Result(column="sex", property="sex", jdbcType=JdbcType.DECIMAL),
            @Result(column="createTime", property="createtime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="modifyTime", property="modifytime", jdbcType=JdbcType.TIMESTAMP)
    })
    ArrayList<Student> getAllSutdent();
}