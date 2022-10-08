package com.example.zhongjianyusuan;

import oracle.sql.TIMESTAMP;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author wangxing
 * @date 2022/10/8
 **/
public enum OracleDataType {
    TIMESTAMP_3 {
        @Override
        public void doPreparedStatementSet(PreparedStatement ps, int index, Object value) throws SQLException {
            if (value == null) {
                ps.setTimestamp(index, null);
                return;
            }
            ps.setTimestamp(index, ((TIMESTAMP) value).timestampValue());
        }
    },
    NUMBER {
        @Override
        public void doPreparedStatementSet(PreparedStatement ps, int index, Object value) throws SQLException {
            ps.setBigDecimal(index, (BigDecimal) value);
        }
    },
    NVARCHAR2 {
        @Override
        public void doPreparedStatementSet(PreparedStatement ps, int index, Object value) throws SQLException {
            ps.setString(index, (String) value);
        }
    },
    RAW {
        @Override
        public void doPreparedStatementSet(PreparedStatement ps, int index, Object value) throws SQLException {
            ps.setBytes(index, (byte[]) value);
        }
    };

    public void doPreparedStatementSet(PreparedStatement ps, int index, Object value) throws SQLException {
    }

    ;

    public static OracleDataType get(String value) {
        switch (value) {
            case "TIMESTAMP(3)":
                return TIMESTAMP_3;
            case "NUMBER":
                return NUMBER;
            case "NVARCHAR2":
                return NVARCHAR2;
            case "RAW":
                return RAW;
        }
        throw new RuntimeException("需要支持的数据类型【" + value + "】");
    }
}
