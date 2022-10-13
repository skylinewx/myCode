package com.example.zhongjianyusuan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangxing
 * @date 2022/9/1
 **/
public class MySplitTableTask2 extends ForkJoinTask<String> {

    private static Logger logger = LoggerFactory.getLogger(MySplitTableTask2.class);

    private final int splitTime;
    private final int begin;
    private final int end;
    private final String splitTableName;
    private final JdbcTemplate jdbcTemplate;
    private final String fieldStr;
    private final AtomicInteger atomicInteger;

    private final QueueComponent queueComponent;

    public MySplitTableTask2(int splitTime, int begin, int end, String splitTableName, JdbcTemplate jdbcTemplate, String fieldStr, AtomicInteger atomicInteger, QueueComponent queueComponent) {
        this.splitTime = splitTime;
        this.begin = begin;
        this.end = end;
        this.splitTableName = splitTableName;
        this.jdbcTemplate = jdbcTemplate;
        this.fieldStr = fieldStr;
        this.atomicInteger = atomicInteger;
        this.queueComponent = queueComponent;
    }

    @Override
    public String getRawResult() {
        return null;
    }

    @Override
    protected void setRawResult(String value) {
        System.out.println("value = " + value);
    }

    @Override
    protected boolean exec() {
        if (splitTime <= 7) {
            int center = (begin + end) / 2;
            int newSplitTime = splitTime + 1;
            MySplitTableTask2 first = new MySplitTableTask2(newSplitTime, begin, center, splitTableName, jdbcTemplate, fieldStr, atomicInteger, queueComponent);
            MySplitTableTask2 second = new MySplitTableTask2(newSplitTime, center, end, splitTableName, jdbcTemplate, fieldStr, atomicInteger, queueComponent);
            first.fork();
            second.fork();
            first.join();
            second.join();
        } else {
            String tableName = splitTableName + "_" + atomicInteger.getAndIncrement();
            String sql = "create table " + tableName + " as select ROWNUM as no" + fieldStr + " from " + splitTableName + " t where t.no>" + begin + " and t.no<=" + end;
            logger.info("开始创建子表【{}】，SQL：【{}】", tableName, sql);
            LocalDateTime start = LocalDateTime.now();
            try {
                jdbcTemplate.execute(sql);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            logger.info("子表【{}】创建完毕，耗时：【{}】", tableName, Duration.between(start, LocalDateTime.now()));

            start = LocalDateTime.now();
            sql = "create index ind_" + tableName + "_DVZ on " + tableName + " (DATATIME,VERSION,ZBTYPE)";
            jdbcTemplate.execute(sql);
            logger.info("子表【{}】索引创建完毕，耗时：【{}】", tableName, Duration.between(start, LocalDateTime.now()));

            SplitTableDTO splitTableDTO = new SplitTableDTO();
            splitTableDTO.setOriTableName(splitTableName);
            splitTableDTO.setChildTableName(tableName);
            try {
                queueComponent.pushTableName(splitTableDTO);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
