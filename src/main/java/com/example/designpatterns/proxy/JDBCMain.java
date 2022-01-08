package com.example.designpatterns.proxy;

import com.mysql.cj.jdbc.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 代理
 * @author skyline
 */
public class JDBCMain {
    private static final Logger logger = LoggerFactory.getLogger(JDBCMain.class);
    public static void main(String[] args) throws SQLException {
        logger.info("开始加载数据库驱动");
        Driver driver = new Driver();
        logger.info("数据库驱动加载成功，{}",ConnectionConst.DRIVER_CLASS_NAME );
        Properties properties = new Properties();
        properties.setProperty("user", ConnectionConst.USER);
        properties.setProperty("password", ConnectionConst.PASSWORD);
        logger.info("开始获取数据库连接");
        Connection connect = driver.connect(ConnectionConst.URL, properties);
        logger.info("数据库连接获取成功,{}",connect);
        PreparedStatement preparedStatement = connect.prepareStatement("select id,name,mathScore,languageScore,englishScore from student");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            logger.info("id:{},name:{}",resultSet.getString(1),resultSet.getString(2));
        }
        resultSet.close();
        logger.info("关闭resultSet");
        preparedStatement.close();
        logger.info("关闭preparedStatement");
        connect.close();
        logger.info("关闭connect");
    }
}
