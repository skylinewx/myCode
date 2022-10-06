package com.example.splittable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangxing
 * @date 2022/9/13
 **/
@RestController
public class MySplitTableController {

    @Autowired
    private SplitTableService splitTableService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/splitTable/{tableName}")
    public String splitTable(@PathVariable String tableName) {
        String sql = "select table_name as \"TABLE_NAME\",status as \"STATUS\" from SPLIT_TABLE_PROCESS where table_name='" + tableName + "'";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        if (!sqlRowSet.next()) {
            splitTableService.doSplit2(tableName);
            return tableName + "开始处理";
        }
        return tableName + "已经在处理中！";
    }

    @GetMapping("/splitTables/{tableNames}")
    public String splitTables(@PathVariable String tableNames) {
        String[] split = tableNames.split(",");
        StringBuilder builder = new StringBuilder();
        for (String tableName : split) {
            if (!StringUtils.hasText(tableName)) {
                continue;
            }
            String sql = "select table_name as \"TABLE_NAME\",status as \"STATUS\" from SPLIT_TABLE_PROCESS where table_name='" + tableName + "'";
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
            if (!sqlRowSet.next()) {
                splitTableService.doSplit2(tableName);
                builder.append(tableName).append("开始处理\r\n");
            } else {
                builder.append(tableName).append("已经在处理中！\r\n");
            }
        }
        return builder.toString();
    }
}
