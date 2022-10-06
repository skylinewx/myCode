package com.example.splittable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.example.splittable")
@EnableAsync
public class SpringMain {

    private static final Logger logger = LoggerFactory.getLogger(SpringMain.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringMain.class, args);
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
        String sql = "select 1 from USER_TABLES where TABLE_NAME='SPLIT_TABLE_PROCESS'";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        if (!sqlRowSet.next()) {
            sql = "create table SPLIT_TABLE_PROCESS(\n" +
                    "    table_name nvarchar2(100) primary key ,\n" +
                    "    status varchar2(20)\n" +
                    ")";
            jdbcTemplate.execute(sql);
        }
        logger.info("启动完毕");
    }


}