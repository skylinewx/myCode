package com.example.designpatterns.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 一个链接的代理
 *
 * @author skyline
 */
public class ConnectionProxy implements Connection {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionProxy.class);
    private final ConnectionPool connectionPool;
    private Connection deleget;
    private boolean close = false;

    public ConnectionProxy(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    void setClose(boolean close) {
        this.close = close;
    }

    void physicsClose(){
        close = true;
        if (deleget != null) {
            try {
                deleget.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            deleget = null;
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        return close;
    }

    @Override
    public void close() throws SQLException {
        if(!close){
            close=true;
            if (connectionPool.isClose()) {
                physicsClose();
                logger.info("由于连接池关闭，所以连接直接关闭,{}", this);
            }else {
                connectionPool.getConnections().add(this);
                logger.info("连接归还,{}", this);
            }
        }
    }

    /**
     * 获取真实连接
     * @return
     * @throws SQLException
     */
    private Connection getPhysicsConnection() throws SQLException {
        if (close) {
            throw new RuntimeException("connection is closed!");
        }
        if (deleget == null) {
            deleget = connectionPool.getDriver().connect(connectionPool.getUrl(), connectionPool.getProperties());
        }
        return deleget;
    }

    @Override
    public Statement createStatement() throws SQLException {
        return getPhysicsConnection().createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return getPhysicsConnection().prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return getPhysicsConnection().prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return getPhysicsConnection().nativeSQL(sql);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return getPhysicsConnection().getAutoCommit();
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        getPhysicsConnection().setAutoCommit(autoCommit);
    }

    @Override
    public void commit() throws SQLException {
        getPhysicsConnection().commit();
    }

    @Override
    public void rollback() throws SQLException {
        getPhysicsConnection().rollback();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return getPhysicsConnection().getMetaData();
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return getPhysicsConnection().isReadOnly();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
getPhysicsConnection().setReadOnly(readOnly);
    }

    @Override
    public String getCatalog() throws SQLException {
        return getPhysicsConnection().getCatalog();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        getPhysicsConnection().setCatalog(catalog);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return getPhysicsConnection().getTransactionIsolation();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        getPhysicsConnection().setTransactionIsolation(level);
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return getPhysicsConnection().getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        getPhysicsConnection().clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return getPhysicsConnection().createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getPhysicsConnection().prepareStatement(sql,resultSetType,resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getPhysicsConnection().prepareCall(sql,resultSetType,resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return getPhysicsConnection().getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        getPhysicsConnection().setTypeMap(map);
    }

    @Override
    public int getHoldability() throws SQLException {
        return getPhysicsConnection().getHoldability();
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        getPhysicsConnection().setHoldability(holdability);
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return getPhysicsConnection().setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return getPhysicsConnection().setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        getPhysicsConnection().rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        getPhysicsConnection().releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getPhysicsConnection().createStatement(resultSetType,resultSetConcurrency,resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getPhysicsConnection().prepareStatement(sql,resultSetType,resultSetConcurrency,resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getPhysicsConnection().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return getPhysicsConnection().prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return getPhysicsConnection().prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return getPhysicsConnection().prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return getPhysicsConnection().createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return getPhysicsConnection().createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return getPhysicsConnection().createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return getPhysicsConnection().createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return getPhysicsConnection().isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        try {
            getPhysicsConnection().setClientInfo(name,value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return getPhysicsConnection().getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return getPhysicsConnection().getClientInfo();
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        try {
            getPhysicsConnection().setClientInfo(properties);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return getPhysicsConnection().createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return getPhysicsConnection().createStruct(typeName, attributes);
    }

    @Override
    public String getSchema() throws SQLException {
        return getPhysicsConnection().getSchema();
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        getPhysicsConnection().setSchema(schema);
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        getPhysicsConnection().abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        getPhysicsConnection().setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return getPhysicsConnection().getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return getPhysicsConnection().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return getPhysicsConnection().isWrapperFor(iface);
    }
}
