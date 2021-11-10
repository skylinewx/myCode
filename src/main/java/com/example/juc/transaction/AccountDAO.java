package com.example.juc.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 账户dao
 *
 * @author 王星
 */
@Repository
public class AccountDAO {

    private static final String sql = "update accountTest set money=money+? where id=?";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 更新账户信息
     *
     * @param changeDTO
     */
    public void updateAccount(AccountChangeDTO changeDTO) {
        jdbcTemplate.update(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setBigDecimal(1, changeDTO.getChangeMoney());
                ps.setString(2, changeDTO.getUserId());
            }
        });
    }
}
