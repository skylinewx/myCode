package com.example.designpatterns.proxy;

import com.mysql.cj.jdbc.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 数据库连接池
 * @author skyline
 */
public class ConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);
    private final Driver driver;
    private final Properties properties;
    private final String url;
    private final ArrayBlockingQueue<ConnectionProxy> connections;
    private volatile boolean close;

    /**
     * 创建一个数据库连接池
     * @param url 连接url
     * @param userName 用户名
     * @param password 密码
     * @param coreSize 连接池中连接的个数
     * @throws SQLException
     */
    public ConnectionPool(String url, String userName, String password, int coreSize) throws SQLException {
        driver = new Driver();
        this.url = url;
        properties = new Properties();
        properties.setProperty("user", userName);
        properties.setProperty("password", password);
        logger.info("数据库驱动加载成功，{}",ConnectionConst.DRIVER_CLASS_NAME );
        connections = new ArrayBlockingQueue<>(coreSize);
        for (int i = 0; i < coreSize; i++) {
            connections.add(new ConnectionProxy(this));
        }
    }

    public boolean isClose(){
        return close;
    }

    public void close(){
        close = true;
        ConnectionProxy poll = connections.poll();
        while (poll!=null) {
            poll.physicsClose();
            poll = connections.poll();
        }
    }

    public Connection getConnection() throws InterruptedException {
        if (close) {
            throw new RuntimeException("connection pool is closed!");
        }
        ConnectionProxy take = connections.take();
        take.setClose(false);
        logger.info("获取链接,{}", take);
        return take;
    }

    public Connection getConnection(long time, TimeUnit timeUnit) throws InterruptedException {
        if (close) {
            throw new RuntimeException("connection pool is closed!");
        }
        ConnectionProxy poll = connections.poll(time, timeUnit);
        if (poll == null) {
            return null;
        }
        poll.setClose(false);
        logger.info("获取链接,{}", poll);
        return poll;
    }

    Driver getDriver() {
        return driver;
    }

    Properties getProperties() {
        return properties;
    }

    String getUrl() {
        return url;
    }

    ArrayBlockingQueue<ConnectionProxy> getConnections() {
        return connections;
    }
}
