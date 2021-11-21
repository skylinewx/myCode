package com.example.juc.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * create table student(
 * id varchar(30) primary key ,
 * name varchar(6),
 * mathScore int(2),
 * languageScore int(2),
 * englishScore int(2)
 * );
 * <p>
 * insert into student values ('001','张三',null,null,null);
 * insert into student values ('002','李四',null,null,null);
 * insert into student values ('003','王五',null,null,null);
 * insert into student values ('004','赵柳',null,null,null);
 * insert into student values ('005','方方',null,null,null);
 * insert into student values ('006','圆圆',null,null,null);
 * insert into student values ('007','三角',null,null,null);
 * insert into student values ('008','豆包',null,null,null);
 * insert into student values ('009','月亮',null,null,null);
 * insert into student values ('010','太阳',null,null,null);
 *
 * @author wangxing
 */
@Repository
public class StudentDAO {

    private static final Logger logger = LoggerFactory.getLogger(StudentDAO.class);

    private static final String GET_STUDENT_BY_ID_SQL = "select id,name,mathScore,languageScore,englishScore from student where id=?";
    private static final String UPDATE_STUDENT_MATHSCORE_SQL = "update student set mathScore=? where id=?";
    private static final String UPDATE_STUDENT_LANGUAGESCORE_SQL = "update student set languageScore=? where id=?";
    private static final String UPDATE_STUDENT_ENGLISHSCORE_SQL = "update student set englishScore=? where id=?";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public StudentDO getById(String id) {
        Assert.notNull(id, "id不能为空");
        logger.info("execute query [{}] [{}]", GET_STUDENT_BY_ID_SQL, id);
        return jdbcTemplate.query(GET_STUDENT_BY_ID_SQL, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, id);
            }
        }, rs -> {
            if (rs.next()) {
                StudentDO studentDO = new StudentDO();
                studentDO.setId(rs.getString("id"));
                studentDO.setName(rs.getString("name"));
                studentDO.setMathScore(rs.getObject("mathScore", Integer.class));
                studentDO.setLanguageScore(rs.getObject("languageScore", Integer.class));
                studentDO.setEnglishScore(rs.getObject("englishScore", Integer.class));
                return studentDO;
            }
            return null;
        });

    }

}
