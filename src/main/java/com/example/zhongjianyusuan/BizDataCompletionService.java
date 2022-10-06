package com.example.zhongjianyusuan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 业务数据补全服务
 */
@Service
public class BizDataCompletionService {

    private static final Logger logger = LoggerFactory.getLogger(BizDataCompletionService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public List<HyperModalDO> getModalList() {
        String sql = "select CODE,NAME,PERIODTYPE,MODELTYPE,PUBLISHSTATE,DIMS,MEAS from BUD_MODEL";
        logger.info("开始查询模型元数据信息，sql：{}", sql);
        LocalDateTime begin = LocalDateTime.now();
        List<HyperModalDO> query = jdbcTemplate.query(sql, new RowMapper<HyperModalDO>() {
            @Override
            public HyperModalDO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return getHyperModalDOByRs(rs);
            }
        });
        logger.info("模型元数据信息查询完毕，耗时：{}", Duration.between(begin, LocalDateTime.now()));
        return query;
    }

    private HyperModalDO getHyperModalDOByRs(ResultSet rs) throws SQLException {
        String code = rs.getString(1);
        String name = rs.getString(2);
        String periodTypeStr = rs.getString(3);
        String modelTypeStr = rs.getString(4);
        String publishStateStr = rs.getString(5);
        String dimStr = rs.getString(6);
        String measStr = rs.getString(7);
        HyperModalDO hyperModalDO = new HyperModalDO();
        hyperModalDO.setCode(code);
        hyperModalDO.setName(name);
        hyperModalDO.setModalType(ModalType.valueOf(modelTypeStr));
        hyperModalDO.setPublishState(PublishState.valueOf(publishStateStr));
        try {
            List<ModalField> modalFields = objectMapper.readValue(dimStr, new TypeReference<List<ModalField>>() {
            });
            hyperModalDO.setDimList(modalFields);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        try {
            List<ModalField> modalFields = objectMapper.readValue(measStr, new TypeReference<List<ModalField>>() {
            });
            hyperModalDO.setMeas(modalFields);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (StringUtils.hasText(periodTypeStr)) {
            String[] split = periodTypeStr.split(";");
            hyperModalDO.setPeriodType(Arrays.asList(split));
        }
        return hyperModalDO;
    }

    @Async("mySplitTableThreadPool")
    public void doDataCompletionAsyncByModal(String modalName) {
        if (!StringUtils.hasText(modalName)) {
            throw new RuntimeException("模型标识为空，执行失败！");
        }
        HyperModalDO hyperModalDO = getHyperModalDOByCode(modalName);
        if (hyperModalDO == null) {
            logger.info("没有找到标识为{}的预算模型", modalName);
            return;
        }
        doDataCompletionByModalList(Collections.singletonList(hyperModalDO));
    }

    private HyperModalDO getHyperModalDOByCode(String modalName) {
        String sql = "select CODE,NAME,PERIODTYPE,MODELTYPE,PUBLISHSTATE,DIMS,MEAS from BUD_MODEL where CODE=?";
        logger.info("开始查询模型信息，sql:[{}]，参数:[{}]", sql, modalName);
        return jdbcTemplate.query(sql, ps -> ps.setString(1, modalName), rs -> {
            if (rs.next()) {
                return getHyperModalDOByRs(rs);
            }
            return null;
        });
    }

    public void doDataCompletionAsyncByModals(String modalNames) {
        String[] modalNameList = modalNames.split(",");
        List<HyperModalDO> modalList = new ArrayList<>(modalNameList.length);
        for (String modalName : modalNameList) {
            HyperModalDO hyperModalDO = getHyperModalDOByCode(modalName);
            if (hyperModalDO == null) {
                logger.info("没有找到标识为{}的预算模型", modalName);
                continue;
            }
            modalList.add(hyperModalDO);
        }
        doDataCompletionByModalList(modalList);
    }

    @Async("mySplitTableThreadPool")
    public void doDataCompletion() {
        List<HyperModalDO> modalList = getModalList();
        doDataCompletionByModalList(modalList);
    }

    private void doDataCompletionByModalList(List<HyperModalDO> modalList) {
        int size = modalList.size();
        HyperModalDO hyperModalDO;
        LocalDateTime start;
        LocalDateTime begin = LocalDateTime.now();
        List<String> failList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            hyperModalDO = modalList.get(i);
            String code = hyperModalDO.getCode();
            String name = hyperModalDO.getName();
            logger.info("({}/{})开始补全{}[{}]的业务数据", i + 1, size, name, code);
            PublishState publishState = hyperModalDO.getPublishState();
            if (publishState != PublishState.PUBLISHED) {
                logger.info("模型{}[{}]处于未发布状态，已忽略", name, code);
                continue;
            }
            start = LocalDateTime.now();
            boolean success = false;
            switch (hyperModalDO.getModalType()) {
                case ZB:
                    success = doZbTableDataCompletion(hyperModalDO);
                    break;
                case FLOAT:
                    success = doFlatTableDataCompletion(hyperModalDO);
                    break;
                case HYPER:
                    success = doHyperTableDataCompletion(hyperModalDO);
                    break;
            }
            if (!success) {
                failList.add(hyperModalDO.getCode());
                logger.info("({}/{}){}[{}]的业务数据补全失败，耗时：[{}]", i + 1, size, name, code, Duration.between(start, LocalDateTime.now()));
            } else {
                logger.info("({}/{}){}[{}]的业务数据补全成功，耗时：[{}]", i + 1, size, name, code, Duration.between(start, LocalDateTime.now()));
            }
        }
        logger.info("数据补全完毕，总耗时：[{}]，失败[{}]个模型，失败详情：{}", Duration.between(begin, LocalDateTime.now()), failList.size(), failList);
    }

    private boolean doHyperTableDataCompletion(HyperModalDO hyperModalDO) {
        String sqlStr = getMergeSql(hyperModalDO, "%s", true);
        String sql;
        List<String> periodType = hyperModalDO.getPeriodType();
        if (periodType == null || periodType.isEmpty()) {
            logger.info("{}[{}]的时期类型为空，已跳过", hyperModalDO.getName(), hyperModalDO.getCode());
            return false;
        }
        boolean success = true;
        for (String pType : periodType) {
            String tableName = hyperModalDO.getCode() + "_" + pType;
            sql = String.format(sqlStr, tableName, tableName);
            boolean b = exeDataCompletionSql(sql, tableName);
            success = b && success;
        }
        return success;
    }

    private boolean exeDataCompletionSql(String sql, String tableName) {
        LocalDateTime start = LocalDateTime.now();
        logger.info("开始补全[{}],sql:{}", tableName, sql);
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            logger.error("[" + tableName + "]sql执行失败：" + e.getMessage(), e);
            return false;
        }
        logger.info("[{}]补全完毕，耗时:[{}]", tableName, Duration.between(start, LocalDateTime.now()));
        return true;
    }

    private String getMergeSql(HyperModalDO hyperModalDO, String tableName, boolean hasOrder) {
        List<ModalField> dimList = hyperModalDO.getDimList();
        int size = dimList.size();
        List<ModalField> meas = hyperModalDO.getMeas();
        StringBuilder sqlBuilder = new StringBuilder(1500);
        sqlBuilder.append("merge into ").append(tableName).append(" a \r\n");
        sqlBuilder.append("using (select ");
        if (hasOrder) {
            sqlBuilder.append("bizkeyorder,floatorder,");
        }
        for (ModalField modalField : dimList) {
            String code = modalField.getCode();
            String dbCode = modalField.getDbCode();
            if (code.equals("MD_STAGE")) {
                sqlBuilder.append("'FINAL' as ");
            }
            sqlBuilder.append(dbCode).append(',');
        }
        for (ModalField modalField : meas) {
            sqlBuilder.append(modalField.getDbCode()).append(',');
        }
        sqlBuilder.setCharAt(sqlBuilder.length() - 1, ' ');
        sqlBuilder.append("from ").append(tableName).append(" where MD_STAGE = 'LATEST') b \r\n");
        sqlBuilder.append("on (");
        for (int i = 0; i < size; i++) {
            ModalField modalField = dimList.get(i);
            String dbCode = modalField.getDbCode();
            sqlBuilder.append("a.").append(dbCode).append("=b.").append(dbCode);
            if (i != size - 1) {
                sqlBuilder.append(" and ");
            }
        }
        sqlBuilder.append(")\r\n when not matched then \r\n insert (");
        if (hasOrder) {
            sqlBuilder.append("bizkeyorder, floatorder,");
        }
        for (ModalField modalField : dimList) {
            String dbCode = modalField.getDbCode();
            sqlBuilder.append(dbCode).append(',');
        }
        for (ModalField modalField : meas) {
            sqlBuilder.append(modalField.getDbCode()).append(',');
        }
        sqlBuilder.setCharAt(sqlBuilder.length() - 1, ')');
        sqlBuilder.append(" \r\n values (");
        if (hasOrder) {
            sqlBuilder.append("sys_guid(), b.floatorder,");
        }
        for (ModalField modalField : dimList) {
            String dbCode = modalField.getDbCode();
            sqlBuilder.append("b.").append(dbCode).append(',');
        }
        for (ModalField modalField : meas) {
            sqlBuilder.append("b.").append(modalField.getDbCode()).append(',');
        }
        sqlBuilder.setCharAt(sqlBuilder.length() - 1, ')');
        return sqlBuilder.toString();
    }

    private boolean doFlatTableDataCompletion(HyperModalDO hyperModalDO) {
        String mergeSql = getMergeSql(hyperModalDO, hyperModalDO.getCode(), true);
        return exeDataCompletionSql(mergeSql, hyperModalDO.getCode());
    }

    private boolean doZbTableDataCompletion(HyperModalDO hyperModalDO) {
        String mergeSql = getMergeSql(hyperModalDO, hyperModalDO.getCode(), false);
        return exeDataCompletionSql(mergeSql, hyperModalDO.getCode());
    }
}
