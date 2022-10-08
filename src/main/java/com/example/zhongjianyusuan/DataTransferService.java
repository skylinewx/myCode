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
import java.util.concurrent.ArrayBlockingQueue;
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
    @Qualifier("myDataTransferThreadPool")
    private ThreadPoolExecutor threadPoolExecutor;

    public void tryTransferData(String sourceTable, String tarTable) {
        logger.info("开始清空{}的业务数据", tarTable);
        jdbcTemplate.execute("truncate table " + tarTable);
        logger.info("{}的业务数据清空完毕", tarTable);
        logger.info("开始查询源表{}元数据信息", sourceTable);
        LocalDateTime startTime = LocalDateTime.now();
        String sql = "select DATA_TYPE as \"dataType\",COLUMN_NAME as \"columnName\" from USER_TAB_COLUMNS where TABLE_NAME=?";
        List<Map<String, Object>> tableColumnList = jdbcTemplate.queryForList(sql, sourceTable);
        logger.info("{}元数据信息查询完毕，耗时：[{}]", sourceTable, Duration.between(startTime, LocalDateTime.now()));
        List<String> columnList = new ArrayList<>(tableColumnList.size());
        List<OracleDataType> dataTypeList = new ArrayList<>(tableColumnList.size());
        for (Map<String, Object> columnMap : tableColumnList) {
            columnList.add((String) columnMap.get("columnName"));
            OracleDataType dataType = OracleDataType.get((String) columnMap.get("dataType"));
            dataTypeList.add(dataType);
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        for (String column : columnList) {
            queryBuilder.append(column).append(" as ").append(column).append(",");
        }
        queryBuilder.setCharAt(queryBuilder.length() - 1, ' ');
        queryBuilder.append("from ").append(sourceTable);
        StringBuilder insertBuilder = new StringBuilder();
        insertBuilder.append("insert into ").append(tarTable).append("(");
        for (String column : columnList) {
            insertBuilder.append(column).append(",");
        }
        insertBuilder.setCharAt(insertBuilder.length() - 1, ')');
        insertBuilder.append(" VALUES (");
        for (String column : columnList) {
            insertBuilder.append("?,");
        }
        insertBuilder.setCharAt(insertBuilder.length() - 1, ')');
        String insertSql = insertBuilder.toString();
        jdbcTemplate.setFetchSize(2000);
        int size = columnList.size();
        startTime = LocalDateTime.now();
        logger.info("开始查询{}的业务数据，sql:[{}]", sourceTable, queryBuilder);
        jdbcTemplate.query(queryBuilder.toString(), new ResultSetExtractor<Object>() {

            List<Map<String, Object>> dataList = new ArrayList<>();
            int count = 0;
            final int batchSize = 1000;
            int insertCount = 1;

            @Override
            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    if (count == batchSize) {
                        insertData2TarTable();
                    }
                    Map<String, Object> dataMap = new HashMap<>();
                    for (int j = 0; j < size; j++) {
                        dataMap.put(columnList.get(j), rs.getObject(j + 1));
                    }
                    dataList.add(dataMap);
                    count++;
                }
                if (count != 0) {
                    insertData2TarTable();
                }
                return null;
            }

            private void insertData2TarTable() {
                LocalDateTime begin = LocalDateTime.now();
                logger.info("开始执行第{}次数据插入", insertCount);
                jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Map<String, Object> stringObjectMap = dataList.get(i);
                        for (int j = 1; j <= size; j++) {
                            int index = j - 1;
                            OracleDataType oracleDataType = dataTypeList.get(index);
                            oracleDataType.doPreparedStatementSet(ps, j, stringObjectMap.get(columnList.get(index)));
                        }
                    }

                    @Override
                    public int getBatchSize() {
                        return count;
                    }
                });
                logger.info("第{}次插入完毕，耗时:[{}]", insertCount, Duration.between(begin, LocalDateTime.now()));
                insertCount++;
                dataList = new ArrayList<>();
                count = 0;
            }
        });
        logger.info("{}的业务数据迁移完毕，耗时:[{}]", sourceTable, Duration.between(startTime, LocalDateTime.now()));
    }

    public void tryTransferData2(String sourceTable, String tarTable) {
        logger.info("开始清空{}的业务数据", tarTable);
        jdbcTemplate.execute("truncate table " + tarTable);
        logger.info("{}的业务数据清空完毕", tarTable);
        dropBackTables(sourceTable);
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
        queryBuilder.setCharAt(queryBuilder.length() - 1, ' ');
        queryBuilder.append("from ").append(sourceTable);
        StringBuilder insertBuilder = new StringBuilder();
        insertBuilder.append("insert  /*+ append */ into %s (");
//        insertBuilder.append("insert into %s (");
        for (String column : columnList) {
            insertBuilder.append(column).append(",");
        }
        insertBuilder.setCharAt(insertBuilder.length() - 1, ')');
        insertBuilder.append(" VALUES (");
        for (String column : columnList) {
            insertBuilder.append("?,");
        }
        insertBuilder.setCharAt(insertBuilder.length() - 1, ')');
        String insertSql = insertBuilder.toString();
        jdbcTemplate.setFetchSize(2000);
        int size = columnList.size();
        startTime = LocalDateTime.now();
        logger.info("开始查询{}的业务数据，sql:[{}]", sourceTable, queryBuilder);
        ArrayBlockingQueue<DataTransferTask> queue = new ArrayBlockingQueue<>(10);
        LinkedBlockingQueue<String> finishTableList = new LinkedBlockingQueue<>();
        StringBuilder fieldStrBuilder = new StringBuilder();
        for (String column : columnList) {
            fieldStrBuilder.append(column).append(",");
        }
        String fieldStr = fieldStrBuilder.substring(0, fieldStrBuilder.length() - 1);
        jdbcTemplate.query(queryBuilder.toString(), new ResultSetExtractor<Object>() {

            List<Map<String, Object>> dataList = new ArrayList<>();
            final int batchSize = 500;
            final AtomicInteger insertCount = new AtomicInteger(1);
            final AtomicInteger insertFinishCount = new AtomicInteger(1);

            @Override
            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                Thread t2 = new Thread(() -> {
                    while (true) {
                        String take = null;
                        try {
                            take = finishTableList.take();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        StringBuilder sqlBuilder = new StringBuilder();
                        sqlBuilder.append("insert into ").append(tarTable).append("(");
                        sqlBuilder.append(fieldStr).append(") ");
                        sqlBuilder.append("select ").append(fieldStr).append(" from ").append(take);
                        String finalSql = sqlBuilder.toString();
                        LocalDateTime now = LocalDateTime.now();
                        logger.info("开始合并[{}]中的数据到[{}]", take, tarTable);
                        jdbcTemplate.execute(finalSql);
                        logger.info("[{}]中的数据合并到[{}]结束，耗时:[{}]", take, tarTable, Duration.between(now, LocalDateTime.now()));
                        jdbcTemplate.execute("drop table " + take);
                        logger.info("删除临时表[{}]", take);
                        insertFinishCount.incrementAndGet();
                    }
                }, "t2");
                t2.start();
                Thread t1 = new Thread(() -> {
                    while (true) {
                        try {
                            DataTransferTask transferTask = queue.poll(20, TimeUnit.SECONDS);
                            if (transferTask == null) {
                                TimeUnit.SECONDS.sleep(20);
                                continue;
                            }
                            threadPoolExecutor.submit(new Runnable() {
                                @Override
                                public void run() {
                                    insertData2TarTable(transferTask.getDataList(), transferTask.getBatchNum());
                                }
                            });
                        } catch (InterruptedException e) {
                            logger.error(e.getMessage(), e);
                            break;
                        }
                    }
                }, "t1");
                t1.start();
                while (rs.next()) {
                    if (dataList.size() == batchSize) {
                        DataTransferTask transferTask = new DataTransferTask();
                        transferTask.setDataList(dataList);
                        transferTask.setBatchNum(insertCount.getAndIncrement());
                        try {
                            queue.put(transferTask);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        this.dataList = new ArrayList<>();
                    }
                    Map<String, Object> dataMap = new HashMap<>();
                    for (int j = 0; j < size; j++) {
                        dataMap.put(columnList.get(j), rs.getObject(j + 1));
                    }
                    dataList.add(dataMap);
                }
                if (!dataList.isEmpty()) {
                    DataTransferTask transferTask = new DataTransferTask();
                    transferTask.setDataList(dataList);
                    transferTask.setBatchNum(insertCount.getAndIncrement());
                    try {
                        queue.put(transferTask);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    this.dataList = new ArrayList<>();
                }
                LocalDateTime begin = LocalDateTime.now();
                logger.info("开始等待所有子表创建完毕");
                while (insertFinishCount.get() != insertCount.get()) {
                    try {
                        logger.info("已完成/总数：{}/{}", insertFinishCount.get(), insertCount.get());
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                logger.info("所有子表创建完毕，共等待[{}]", Duration.between(begin, LocalDateTime.now()));
//                StringBuilder sqlBuilder = new StringBuilder();
//                sqlBuilder.append("insert into ").append(tarTable).append("(");
//                StringBuilder fieldStrBuilder = new StringBuilder();
//                for (String column : columnList) {
//                    fieldStrBuilder.append(column).append(",");
//                }
//                String fieldStr = fieldStrBuilder.substring(0, fieldStrBuilder.length() - 1);
//                sqlBuilder.append(fieldStr).append(") (");
//                for (int i = 1; i < insertCount.get(); i++) {
//                    sqlBuilder.append("select ").append(fieldStr).append(" from ").append(sourceTable).append("_back_").append(i);
//                    if (i != insertCount.get() - 1) {
//                        sqlBuilder.append("\r\n union all \r\n");
//                    }
//                }
//                sqlBuilder.append(")");
//                String finalSql = sqlBuilder.toString();
//                logger.info("开始执行最后的数据合并，sql:[{}]", finalSql);
//                begin = LocalDateTime.now();
//                jdbcTemplate.execute(finalSql);
//                logger.info("{}数据合并完毕，耗时:[{}]", tarTable, Duration.between(begin, LocalDateTime.now()));
                threadPoolExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        dropBackTables(sourceTable);
                    }
                });
                return null;
            }

            private void insertData2TarTable(List<Map<String, Object>> dataList, int batchNum) {
                LocalDateTime begin = LocalDateTime.now();
                String splitTableName = sourceTable + "_back_" + batchNum;
                logger.info("开始创建数据临时表{}", splitTableName);
                jdbcTemplate.execute("create table " + splitTableName + " as select * from " + sourceTable + " where 1=2");
                jdbcTemplate.execute("alter table " + splitTableName + " nologging");
                logger.info("数据临时表{}创建成功，耗时:[{}]", splitTableName, Duration.between(begin, LocalDateTime.now()));
                begin = LocalDateTime.now();
                logger.info("开始执行第{}次数据插入，本次插入数量[{}]", batchNum, dataList.size());
                String exeInsertSql = String.format(insertSql, splitTableName);
                jdbcTemplate.batchUpdate(exeInsertSql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Map<String, Object> stringObjectMap = dataList.get(i);
                        for (int j = 1; j <= size; j++) {
                            ps.setObject(j, stringObjectMap.get(columnList.get(j - 1)));
                        }
                    }

                    @Override
                    public int getBatchSize() {
                        return dataList.size();
                    }
                });
                logger.info("第{}次插入完毕，耗时:[{}]", batchNum, Duration.between(begin, LocalDateTime.now()));
                try {
                    finishTableList.put(splitTableName);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        logger.info("{}的业务数据迁移完毕，耗时:[{}]", sourceTable, Duration.between(startTime, LocalDateTime.now()));
    }

    private void dropBackTables(String sourceTable) {
        List<String> backTables = jdbcTemplate.queryForList("select table_name from user_tables where table_name like '%" + sourceTable + "_BACK_%'", String.class);
        for (String backTable : backTables) {
            jdbcTemplate.execute("drop table " + backTable);
            logger.info("清空已存在的数据备份表{}", backTable);
        }
    }
}
