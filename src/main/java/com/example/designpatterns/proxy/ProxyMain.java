package com.example.designpatterns.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 代理模式<br/>
 * 代理模式与装饰模式的区别，代理模式中的代理对象是程序内部生成，客户端依赖接口调用，零感知。<br/>
 * 装饰模式由客户端发起嵌套，按需嵌套。
 */
public class ProxyMain {
    private static final Logger logger = LoggerFactory.getLogger(ProxyMain.class);
    public static void main(String[] args) throws SQLException, InterruptedException {
        int coreSize =2;
        ConnectionPool connectionPool = new ConnectionPool(ConnectionConst.URL, ConnectionConst.USER, ConnectionConst.PASSWORD, coreSize);
        for (int i = 0; i < coreSize*2; i++) {
            Thread thread = new Thread(()->{
                Connection connect = null;
                try {
                    long timeMillis = System.currentTimeMillis();
                    logger.info("开始获取连接");
                    connect = connectionPool.getConnection();
                    logger.info("[{}]毫秒后，数据库连接获取成功",System.currentTimeMillis()-timeMillis);
                    PreparedStatement preparedStatement = connect.prepareStatement("select id,name,mathScore,languageScore,englishScore from student");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()){
                        logger.info("id:{},name:{}",resultSet.getString(1),resultSet.getString(2));
                    }
                    resultSet.close();
                    logger.info("关闭resultSet");
                    preparedStatement.close();
                    logger.info("关闭preparedStatement");
                } catch (InterruptedException | SQLException e) {
                    e.printStackTrace();
                }finally {
                    if (connect != null) {
                        try {
                            connect.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        logger.info("关闭connect");
                    }
                }

            },"thread-"+i);
            thread.start();
        }
    }
}
