package com.example.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author wangxing
 * @date 2022/10/10
 **/
@Service
public class DataCopyService {

    private static final Logger logger = LoggerFactory.getLogger(DataCopyService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<String> getOrgList() {
        return jdbcTemplate.queryForList(" select code from md_org where code<>'GT00'", String.class);
    }

    public void doCopy(String tableName) {
        List<String> orgList = getOrgList();
        int size = orgList.size();
        String querySql = "select datatime,md_scene,md_currency,md_mgrver,md_stage,zb_je2,md_cwsdbwd from " + tableName + " where mdcode='GT00'";
        jdbcTemplate.setFetchSize(2000);
        LocalDateTime start = LocalDateTime.now();
        logger.info("开始查询基准数据");
        List<Data> dataList = jdbcTemplate.query(querySql, new RowMapper<Data>() {
            @Override
            public Data mapRow(ResultSet rs, int rowNum) throws SQLException {
                String datatime = rs.getString(1);
                String scene = rs.getString(2);
                String currency = rs.getString(3);
                String mgrver = rs.getString(4);
                String stage = rs.getString(5);
                BigDecimal je2 = rs.getBigDecimal(6);
                String cwsdbwd = rs.getString(7);
                Data data = new Data();
                data.datatime = datatime;
                data.scene = scene;
                data.currency = currency;
                data.mgrver = mgrver;
                data.stage = stage;
                data.je2 = je2;
                data.cwsdbwd = cwsdbwd;
                return data;
            }
        });
        logger.info("基准数据查询完毕，耗时：{}", Duration.between(start, LocalDateTime.now()));
        String insertSql = "insert into " + tableName + " (mdcode,datatime,md_scene,md_currency,md_mgrver,md_stage,bizkeyorder,floatorder,zb_je2,md_cwsdbwd) values (?,?,?,?,?,?,?,?,?,?)";
        for (int i = 0; i < size; i++) {
            String org = orgList.get(i);
            LocalDateTime begin = LocalDateTime.now();
            logger.info("[{}]/[{}],开始插入[{}]的数据", i + 1, size, org);
            jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Data data = dataList.get(i);
                    ps.setString(1, org);
                    ps.setString(2, data.datatime);
                    ps.setString(3, data.scene);
                    ps.setString(4, data.currency);
                    ps.setString(5, data.mgrver);
                    ps.setString(6, data.stage);
                    ps.setString(7, UUID.randomUUID().toString());
                    ps.setInt(8, -1);
                    ps.setBigDecimal(9, data.je2);
                    ps.setString(10, data.cwsdbwd);
                }

                @Override
                public int getBatchSize() {
                    return dataList.size();
                }
            });
            logger.info("[{}]/[{}],插入完毕，耗时[{}]", i + 1, size, Duration.between(begin, LocalDateTime.now()));
        }
        logger.info("处理结束，耗时[{}]", Duration.between(start, LocalDateTime.now()));
    }


    private static class Data {
        String datatime;
        String scene;
        String currency;
        String mgrver;
        String stage;
        BigDecimal je2;
        String cwsdbwd;
    }
}
