package com.example.splittable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangxing
 * @date 2022/9/1
 **/
@Service
public class SplitTableService {

    private static final Logger logger = LoggerFactory.getLogger(SplitTableService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QueueComponent queueComponent;

    public void doSplit1(String tableName) {
        LocalDateTime startTime = LocalDateTime.now();
        logger.info("=====doSplit1=====开始拆分：[{}]", tableName);
        String fieldStr = getTableFieldStr(tableName);
        AtomicInteger atomicInteger = new AtomicInteger();
        ForkJoinPool forkJoinPool = new ForkJoinPool(64);
        MySplitTableTask1 mySplitTableTask1 = new MySplitTableTask1(1, null, null, tableName, jdbcTemplate, fieldStr, atomicInteger);
        forkJoinPool.invoke(mySplitTableTask1);
        mySplitTableTask1.join();
        logger.info("=====doSplit1=====[{}]拆分完毕，耗时：[{}]", tableName, Duration.between(startTime, LocalDateTime.now()));
    }

    @Async("mySplitTableThreadPool")
    public void doSplit2(String tableName) {
        try {
            jdbcTemplate.execute("insert into SPLIT_TABLE_PROCESS(table_name,status) values ('" + tableName + "','PROCESSING')");
            LocalDateTime begin = LocalDateTime.now();
            logger.info("=====doSplit2=====开始拆分：[{}]", tableName);
            String fieldStr = getTableFieldStr(tableName);
            AtomicInteger atomicInteger = new AtomicInteger();
            String newTable = tableName + "_1";
            String sql = "select 1 from user_tables where table_name='" + newTable + "'";
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
            LocalDateTime startTime;
            if (sqlRowSet.next()) {
                logger.info("基准表已存在：[{}]，跳过创建", newTable);
            } else {
                startTime = LocalDateTime.now();
                sql = "create table " + newTable + " as select ROWNUM as no" + fieldStr + " from " + tableName + " t";
                logger.info("开始创建基准表：[{}]，SQL：[{}]", newTable, sql);
                jdbcTemplate.execute(sql);
                logger.info("基准表[{}]创建完毕，耗时：[{}]", newTable, Duration.between(startTime, LocalDateTime.now()));
            }
            String indexName = "ORDER_" + newTable;
            sql = "select 1 from user_indexes where index_name='" + indexName + "'";
            sqlRowSet = jdbcTemplate.queryForRowSet(sql);
            if (sqlRowSet.next()) {
                logger.info("基准表唯一索引[{}]已存在，跳过创建", indexName);
            } else {
                sql = "create unique index " + indexName + " on " + newTable + " (no)";
                logger.info("开始创建基准表唯一索引：[{}]，SQL：[{}]", indexName, sql);
                startTime = LocalDateTime.now();
                jdbcTemplate.execute(sql);
                logger.info("基准表唯一索引[{}]创建完毕，耗时：[{}]", "ORDER_" + newTable, Duration.between(startTime, LocalDateTime.now()));
            }
            logger.info("开始查询[{}]是否存在已拆分结果", newTable);
            sql = "select table_name from user_tables where table_name like '" + newTable + "_%'";
            List<String> existTableList = jdbcTemplate.queryForList(sql, String.class);
            for (String existTableName : existTableList) {
                sql = "drop table " + existTableName;
                jdbcTemplate.execute(sql);
                logger.info("已删除旧的拆分结果表[{}]", existTableName);
            }
            logger.info("[{}]无存在已拆分结果", newTable);
            sql = "select max(no) from " + newTable;
            logger.info("开始查询基准表[{}]的总数量，SQL：[{}]", newTable, sql);
            startTime = LocalDateTime.now();
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
            logger.info("基准表[{}]的总数量查询，count=[{}]，耗时：[{}]", newTable, count, Duration.between(startTime, LocalDateTime.now()));
            startTime = LocalDateTime.now();
            String versionRelTable = newTable + "_VER_REL";
            sql = "create table " + versionRelTable + "(version NUMBER(10),storeTable varchar2(60))";
            jdbcTemplate.execute(sql);
            logger.info("创建版本映射表[{}],耗时:[{}]", versionRelTable, Duration.between(startTime, LocalDateTime.now()));
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            MySplitTableTask2 mySplitTableTask2 = new MySplitTableTask2(1, -1, count, newTable, jdbcTemplate, fieldStr, atomicInteger, queueComponent);
            forkJoinPool.invoke(mySplitTableTask2);
            mySplitTableTask2.join();
            startTime = LocalDateTime.now();
            sql = "drop table " + newTable;
            logger.info("删除基准表：[{}]，SQL：[{}]", newTable, sql);
            jdbcTemplate.execute(sql);
            logger.info("基准表[{}]删除完毕，耗时：[{}]", newTable, Duration.between(startTime, LocalDateTime.now()));
            logger.info("=====doSplit2=====[{}]拆分完毕，耗时：[{}]", tableName, Duration.between(begin, LocalDateTime.now()));
        } finally {
            jdbcTemplate.execute("delete from SPLIT_TABLE_PROCESS where table_name='" + tableName + "'");
        }
    }

    private String getTableFieldStr(String tableName) {
        String sql = "select COLUMN_NAME from USER_TAB_COLUMNS where TABLE_NAME='" + tableName + "'";
        List<String> tableFields = jdbcTemplate.queryForList(sql, String.class);
        StringBuilder builder = new StringBuilder();
        for (String tableField : tableFields) {
            builder.append(",t.").append(tableField);
        }
        return builder.toString();
    }
}
