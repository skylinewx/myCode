package com.example.splittable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangxing
 * @date 2022/9/2
 **/
@Service
public class VersionRelService {

    private static final Logger logger = LoggerFactory.getLogger(VersionRelService.class);

    @Autowired
    private QueueComponent queueComponent;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void doListen() {
        String name = "versionRelServiceThread";
        int processors = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(processors, processors, 120, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadFactory() {
            private final AtomicInteger atomicInteger = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, name + atomicInteger.getAndIncrement());
            }
        }, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                try {
                    executor.getQueue().put(r);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread versionRelServiceThread = new Thread(() -> {
            while (true) {
                try {
                    SplitTableDTO splitTableDTO = queueComponent.takeTableName();
                    threadPoolExecutor.execute(() -> {
                        String oriTableName = splitTableDTO.getOriTableName();
                        String childTableName = splitTableDTO.getChildTableName();
                        LocalDateTime start = LocalDateTime.now();
                        String sql = "select distinct version from " + childTableName + " where version!=0";
                        List<Integer> integers = jdbcTemplate.queryForList(sql, Integer.class);
                        logger.info("查询【{}】版本信息耗时【{}】，非零版本个数【{}】", childTableName, Duration.between(start, LocalDateTime.now()), integers.size());
                        if (integers.isEmpty()) {
                            return;
                        }
                        start = LocalDateTime.now();
                        sql = "insert into " + oriTableName + "_VER_REL (version,storeTable) values (?,?)";
                        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                            @Override
                            public void setValues(PreparedStatement ps, int i) throws SQLException {
                                Integer versionNo = integers.get(i);
                                ps.setInt(1, versionNo);
                                ps.setString(2, childTableName);
                            }

                            @Override
                            public int getBatchSize() {
                                return integers.size();
                            }
                        });
                        logger.info("【{}】版本信息插入完毕，耗时：【{}】", childTableName, Duration.between(start, LocalDateTime.now()));
                    });
                } catch (Throwable e) {

                }

            }
        }, name);
        versionRelServiceThread.start();
    }
}
