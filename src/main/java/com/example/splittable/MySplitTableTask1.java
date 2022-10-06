package com.example.splittable;

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
public class MySplitTableTask1 extends ForkJoinTask<String> {

    private static Logger logger = LoggerFactory.getLogger(MySplitTableTask1.class);

    private final int splitTime;
    private final Boolean first;
    private final Integer splitTableRowCount;
    private final String splitTableName;
    private final JdbcTemplate jdbcTemplate;
    private final String fieldStr;
    private final AtomicInteger atomicInteger;

    public MySplitTableTask1(int splitTime, Boolean first, Integer splitTableRowCount, String splitTableName, JdbcTemplate jdbcTemplate, String fieldStr, AtomicInteger atomicInteger) {
        this.splitTime = splitTime;
        this.first = first;
        this.splitTableRowCount = splitTableRowCount;
        this.splitTableName = splitTableName;
        this.jdbcTemplate = jdbcTemplate;
        this.fieldStr = fieldStr;
        this.atomicInteger = atomicInteger;
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
        String newTable = splitTableName + "_" + atomicInteger.getAndIncrement();
        String sql = "create table " + newTable + " as select ROWNUM as no" + fieldStr + " from " + splitTableName + " t";
        if (first != null) {
            int half = splitTableRowCount / 2;
            if (first) {
                sql = sql + " where t.no<" + half;
            } else {
                sql = sql + " where t.no>=" + half;
            }
        }
        LocalDateTime startTime = LocalDateTime.now();
        logger.info("开始创建子表：[{}]，SQL：[{}]", newTable, sql);
        jdbcTemplate.execute(sql);
        logger.info("子表[{}]创建完毕，耗时：[{}]", newTable, Duration.between(startTime, LocalDateTime.now()));
        if (splitTime <= 6) {
            sql = "select count(1) from " + newTable;
            logger.info("开始查询[{}]的总数量，SQL：[{}]", newTable, sql);
            startTime = LocalDateTime.now();
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
            logger.info("[{}]的总数量查询，count=[{}]，耗时：[{}]", newTable, count, Duration.between(startTime, LocalDateTime.now()));
            int newSplitTime = splitTime + 1;
            MySplitTableTask1 firstTask = new MySplitTableTask1(newSplitTime, true, count, newTable, jdbcTemplate, fieldStr, atomicInteger);
            MySplitTableTask1 secondTask = new MySplitTableTask1(newSplitTime, false, count, newTable, jdbcTemplate, fieldStr, atomicInteger);
            firstTask.fork();
            secondTask.fork();
            firstTask.join();
            secondTask.join();
            sql = "drop table " + newTable;
            logger.info("子任务执行完毕，删除中间表[{}]，SQL：[{}]", newTable, sql);
            startTime = LocalDateTime.now();
            jdbcTemplate.execute(sql);
            logger.info("中间表[{}]删除完毕，耗时：[{}]", newTable, Duration.between(startTime, LocalDateTime.now()));
        }
        return true;
    }
}
