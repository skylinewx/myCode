package com.example.zhongjianyusuan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DataTransferService {

    private static final Logger logger = LoggerFactory.getLogger(DataTransferService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("mySplitTableThreadPool")
    private ThreadPoolExecutor threadPoolExecutor;

    public void tryTransferData(String sourceTable,String tarTable){
        logger.info("开始清空{}的业务数据", tarTable);
        jdbcTemplate.execute("truncate table "+tarTable);
        logger.info("{}的业务数据清空完毕", tarTable);
        logger.info("开始查询源表{}元数据信息", sourceTable);
        LocalDateTime startTime = LocalDateTime.now();
        String sql = "select COLUMN_NAME from USER_TAB_COLUMNS where TABLE_NAME=?";
        List<String> columnList = jdbcTemplate.queryForList(sql, String.class, sourceTable);
        logger.info("{}元数据信息查询完毕，耗时：[{}]", sourceTable, Duration.between(startTime, LocalDateTime.now()));
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        for (String column : columnList) {
            queryBuilder.append(column).append(" as ").append(column).append(",");
        }
        queryBuilder.setCharAt(queryBuilder.length()-1, ' ');
        queryBuilder.append("from ").append(sourceTable);
        StringBuilder insertBuilder = new StringBuilder();
        insertBuilder.append("insert into ").append(tarTable).append("(");
        for (String column : columnList) {
            insertBuilder.append(column).append(",");
        }
        insertBuilder.setCharAt(insertBuilder.length()-1, ')');
        insertBuilder.append(" VALUES (");
        for (String column : columnList) {
            insertBuilder.append("?,");
        }
        insertBuilder.setCharAt(insertBuilder.length()-1, ')');
        String insertSql = insertBuilder.toString();
        jdbcTemplate.setFetchSize(2000);
        int size = columnList.size();
        startTime = LocalDateTime.now();
        logger.info("开始查询{}的业务数据，sql:[{}]", sourceTable, queryBuilder);
        jdbcTemplate.query(queryBuilder.toString(), new ResultSetExtractor<Object>() {

            List<Map<String,Object>> dataList = new ArrayList<>();
            int count = 0;
            final int batchSize = 5;
            int insertCount = 1;
            @Override
            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()){
                    if (count<batchSize) {
                        Map<String,Object> dataMap = new HashMap<>();
                        for (int j = 0; j < size; j++) {
                            dataMap.put(columnList.get(j), rs.getObject(j+1));
                        }
                        dataList.add(dataMap);
                        count++;
                    }else {
                        insertData2TarTable();
                    }
                }
                if(count!=0){
                    insertData2TarTable();
                }
                return null;
            }

            private void insertData2TarTable() {
                LocalDateTime begin = LocalDateTime.now();
                logger.info("开始执行第{}次数据插入",insertCount);
                jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Map<String, Object> stringObjectMap = dataList.get(i);
                        for (int j = 1; j <= size; j++) {
                            ps.setObject(j, stringObjectMap.get(columnList.get(j-1)));
                        }
                    }

                    @Override
                    public int getBatchSize() {
                        return count;
                    }
                });
                logger.info("第{}次插入完毕，耗时:[{}]",insertCount,Duration.between(begin, LocalDateTime.now()));
                insertCount++;
                dataList = new ArrayList<>();
                count=0;
            }
        });
        logger.info("{}的业务数据迁移完毕，耗时:[{}]",sourceTable,Duration.between(startTime,LocalDateTime.now()));
    }

    public void tryTransferData2(String sourceTable,String tarTable){
        logger.info("开始清空{}的业务数据", tarTable);
        jdbcTemplate.execute("truncate table "+tarTable);
        logger.info("{}的业务数据清空完毕", tarTable);
        List<String> backTables = jdbcTemplate.queryForList("select table_name from user_tables where table_name like '%" + sourceTable + "_BACK_%'", String.class);
        for (String backTable : backTables) {
            jdbcTemplate.execute("drop table "+backTable);
            logger.info("清空已存在的数据备份表{}", backTable);
        }
        logger.info("开始查询源表{}元数据信息", sourceTable);
        LocalDateTime startTime = LocalDateTime.now();
        String sql = "select COLUMN_NAME from USER_TAB_COLUMNS where TABLE_NAME=?";
        List<String> columnList = jdbcTemplate.queryForList(sql, String.class, sourceTable);
        logger.info("{}元数据信息查询完毕，耗时：[{}]", sourceTable, Duration.between(startTime, LocalDateTime.now()));
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        for (String column : columnList) {
            queryBuilder.append(column).append(" as ").append(column).append(",");
        }
        queryBuilder.setCharAt(queryBuilder.length()-1, ' ');
        queryBuilder.append("from ").append(sourceTable);
        StringBuilder insertBuilder = new StringBuilder();
        insertBuilder.append("insert into %s (");
        for (String column : columnList) {
            insertBuilder.append(column).append(",");
        }
        insertBuilder.setCharAt(insertBuilder.length()-1, ')');
        insertBuilder.append(" VALUES (");
        for (String column : columnList) {
            insertBuilder.append("?,");
        }
        insertBuilder.setCharAt(insertBuilder.length()-1, ')');
        String insertSql = insertBuilder.toString();
        jdbcTemplate.setFetchSize(2000);
        int size = columnList.size();
        startTime = LocalDateTime.now();
        logger.info("开始查询{}的业务数据，sql:[{}]", sourceTable, queryBuilder);
        LinkedBlockingQueue<DataTransferTask>  queue = new LinkedBlockingQueue<>();
        jdbcTemplate.query(queryBuilder.toString(), new ResultSetExtractor<Object>() {

            List<Map<String,Object>> dataList = new ArrayList<>();
            int count = 0;
                final int batchSize = 1000;
            final AtomicInteger insertCount = new AtomicInteger(1);
            final AtomicInteger insertFinishCount = new AtomicInteger(1);
            @Override
            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                Thread t1 = new Thread(()->{
                    while (true){
                        try {
                            DataTransferTask transferTask = queue.poll(10, TimeUnit.SECONDS);
                            if (transferTask == null) {
                                break;
                            }
                            threadPoolExecutor.submit(new Runnable() {
                                @Override
                                public void run() {
                                    insertData2TarTable(transferTask.getDataList(), transferTask.getBatchSize(),transferTask.getBatchNum());
                                    insertFinishCount.incrementAndGet();
                                }
                            });
                        } catch (InterruptedException e) {
                            logger.error(e.getMessage(), e);
                            break;
                        }
                    }
                }, "t1");
                t1.start();
                while (rs.next()){
                    if (count<batchSize) {
                        Map<String,Object> dataMap = new HashMap<>();
                        for (int j = 0; j < size; j++) {
                            dataMap.put(columnList.get(j), rs.getObject(j+1));
                        }
                        dataList.add(dataMap);
                        count++;
                    }else {
                        DataTransferTask transferTask = new DataTransferTask();
                        transferTask.setDataList(dataList);
                        transferTask.setBatchNum(insertCount.getAndIncrement());
                        transferTask.setBatchSize(count);
                        try {
                            queue.put(transferTask);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        this.dataList = new ArrayList<>();
                        count=0;
                    }
                }
                if(count!=0){
                    DataTransferTask transferTask = new DataTransferTask();
                    transferTask.setDataList(dataList);
                    transferTask.setBatchNum(insertCount.getAndIncrement());
                    transferTask.setBatchSize(count);
                    try {
                        queue.put(transferTask);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    this.dataList = new ArrayList<>();
                    count=0;
                }
                LocalDateTime begin = LocalDateTime.now();
                logger.info("开始等待所有子表创建完毕");
                while (insertFinishCount.get()!=insertCount.get()){
                    try {
                        logger.info("已完成/总数：{}/{}", insertFinishCount.get(),insertCount.get());
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                logger.info("所有子表创建完毕，共等待[{}]",Duration.between(begin,LocalDateTime.now()));
                StringBuilder sqlBuilder = new StringBuilder();
                sqlBuilder.append("insert into ").append(tarTable).append("(");
                StringBuilder fieldStrBuilder = new StringBuilder();
                for (String column : columnList) {
                    fieldStrBuilder.append(column).append(",");
                }
                String fieldStr = fieldStrBuilder.substring(0, fieldStrBuilder.length() - 1);
                sqlBuilder.append(fieldStr).append(") (");
                for (int i = 1; i < insertCount.get(); i++) {
                    sqlBuilder.append("select ").append(fieldStr).append(" from ").append(sourceTable).append("_back_").append(i);
                    if (i!=insertCount.get()-1){
                        sqlBuilder.append("\r\n union all \r\n");
                    }
                }
                sqlBuilder.append(")");
                String finalSql = sqlBuilder.toString();
                logger.info("开始执行最后的数据合并，sql:[{}]", finalSql);
                 begin = LocalDateTime.now();
                jdbcTemplate.execute(finalSql);
                logger.info("{}数据合并完毕，耗时:[{}]",tarTable,Duration.between(begin, LocalDateTime.now()));
                return null;
            }

            private void insertData2TarTable(List<Map<String, Object>> dataList,int batchSize,int batchNum) {
                LocalDateTime begin = LocalDateTime.now();
                String splitTableName = sourceTable+"_back_"+ batchNum;
                logger.info("开始创建数据临时表{}", splitTableName);
                jdbcTemplate.execute("create table "+splitTableName+" as select * from "+sourceTable+" where 1=2");
                logger.info("数据临时表{}创建成功，耗时:[{}]",splitTableName,Duration.between(begin, LocalDateTime.now()));
                begin = LocalDateTime.now();
                logger.info("开始执行第{}次数据插入",batchNum);
                String exeInsertSql = String.format(insertSql, splitTableName);
                jdbcTemplate.batchUpdate(exeInsertSql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Map<String, Object> stringObjectMap = dataList.get(i);
                        for (int j = 1; j <= size; j++) {
                            ps.setObject(j, stringObjectMap.get(columnList.get(j-1)));
                        }
                    }

                    @Override
                    public int getBatchSize() {
                        return batchSize;
                    }
                });
                logger.info("第{}次插入完毕，耗时:[{}]",batchNum,Duration.between(begin, LocalDateTime.now()));
            }
        });

        logger.info("{}的业务数据迁移完毕，耗时:[{}]",sourceTable,Duration.between(startTime,LocalDateTime.now()));
    }
}
