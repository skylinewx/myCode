package com.example.juc.hikari;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication(scanBasePackages = "com.example.juc.hikari")
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args);
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        logger.info("{}", dataSource);
        int maxConnectionCount = 10;
        List<Connection> connections = new ArrayList<>();
        for (int i = 0; i < maxConnectionCount; i++) {
            try {
                Connection connection = dataSource.getConnection();
                connections.add(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Thread thread = new Thread(() -> {
            Connection connection = null;
            do {
                try {
                    connection = dataSource.getConnection();
                } catch (SQLException e) {
                    logger.error("连接获取失败:[{}]", e.getMessage());
                }
            } while (connection == null);
            logger.info("获取到新的连接了！");
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            logger.info("链接归还了！");
        }, "test");
        thread.start();
        try {
            logger.info("主线程休眠10s");
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        connections.forEach(connection -> {
            try {
                connection.close();
                logger.info("归还了");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }
}
