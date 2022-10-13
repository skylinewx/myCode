package com.example.zhongjianyusuan;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 比较dna模型中01与03的差异
 */
@Service
public class DnaModelDataCompareService {

    private static final Logger logger = LoggerFactory.getLogger(DnaModelDataCompareService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 所有的数值型指标
     *
     * @return
     */
    public Set<String> getNumZbList() {
        jdbcTemplate.setFetchSize(2000);
        List<String> strings = jdbcTemplate.queryForList("select STDCODE from BT_BUDGETINDEX where DATATYPE=1 or DATATYPE=3", String.class);
        return new HashSet<>(strings);
    }

    public List<HyperModalDO> getModalList(List<String> modalCodes) {
        List<HyperModalDO> result;
        StringBuilder sqlBuilder = new StringBuilder("select MODELDATA from BT_HYBERCUBE_MODEL");
        if (modalCodes != null && !modalCodes.isEmpty()) {
            sqlBuilder.append(" where name in (");
            for (String modalCode : modalCodes) {
                sqlBuilder.append("?,");
            }
            sqlBuilder.setCharAt(sqlBuilder.length() - 1, ')');
        }
        result = jdbcTemplate.query(sqlBuilder.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                if (modalCodes != null && !modalCodes.isEmpty()) {
                    for (int i = 0; i < modalCodes.size(); i++) {
                        ps.setString(i + 1, modalCodes.get(i));
                    }
                }
            }
        }, new RowMapper<HyperModalDO>() {

            @Override
            public HyperModalDO mapRow(ResultSet rs, int rowNum) throws SQLException {
                String modelDataXml = rs.getString(1);
                SAXReader reader = new SAXReader();
                Document document;
                try {
                    document = reader.read(new StringReader(modelDataXml));
                } catch (DocumentException e) {
                    logger.error(e.getMessage(), e);
                    return null;
                }
                Element modelDataNode = document.getRootElement();
                String state = getValue(modelDataNode, "state");
                String name = getValue(modelDataNode, "name");
                String title = getValue(modelDataNode, "title");
                if ("0".equals(state)) {
                    logger.info("模型{}尚未发布，已跳过", name);
                    return null;
                }
                HyperModalDO hyperModalDO = new HyperModalDO();
                hyperModalDO.setCode(name);
                hyperModalDO.setName(title);
                Element dimensionsElement = modelDataNode.element("dimensions");
                List<Element> dimElementList = dimensionsElement.elements();
                List<ModalField> dimList = new ArrayList<>(dimElementList.size());
                for (Element element : dimElementList) {
                    String dimName = getValue(element, "name");
                    ModalField modalField = new ModalField();
                    modalField.setCode(dimName);
                    modalField.setDbCode(dimName);
                    dimList.add(modalField);
                }
                hyperModalDO.setDimList(dimList);
                Element measurementsElement = modelDataNode.element("measurements");
                List<Element> measElementList = measurementsElement.elements();
                List<ModalField> measList = new ArrayList<>(measElementList.size());
                for (Element element : measElementList) {
                    String type = getValue(element, "type");
                    //仅数值型或整数型
                    if ("1".equals(type) || "3".equals(type)) {
                        String measCode = getValue(element, "name");
                        ModalField modalField = new ModalField();
                        modalField.setCode(measCode);
                        modalField.setDbCode(measCode);
                        measList.add(modalField);
                    }
                }
                hyperModalDO.setMeas(measList);
                return hyperModalDO;
            }

            private String getValue(Element element, String attributeName) {
                Attribute attribute = element.attribute(attributeName);
                return attribute.getText();
            }
        });
        return result;
    }

    public void doCheckData(List<String> modalCodes) {
        LocalDateTime begin = LocalDateTime.now();
        logger.info("开始查询模型元数据信息");
        List<HyperModalDO> modalList = getModalList(modalCodes);
        logger.info("模型元数据信息查询完毕，耗时[{}]", Duration.between(begin, LocalDateTime.now()));
        StringBuilder sqlBuilder = new StringBuilder();
        for (HyperModalDO hyperModalDO : modalList) {
            logger.info("开始检查[{}]中01与03的数据", hyperModalDO.getCode());
            sqlBuilder.delete(0, sqlBuilder.length());
            sqlBuilder.append("select ");
            List<ModalField> dimList = hyperModalDO.getDimList();
            for (ModalField modalField : dimList) {
                String dbCode = modalField.getDbCode();
                if ("ZBTYPE".equals(dbCode) || "VERSION".equals(dbCode)) {
                    continue;
                }
                sqlBuilder.append("data01.").append(dbCode).append(",");
            }
            sqlBuilder.setCharAt(sqlBuilder.length() - 1, ' ');
            sqlBuilder.append("from ( \r\n(select * from ").append(hyperModalDO.getCode());
            sqlBuilder.append(" where VERSION = 0 and ZBTYPE = 1) data01 \r\n full join \r\n(select * from ").append(hyperModalDO.getCode());
            sqlBuilder.append(" where VERSION = 0 and ZBTYPE = 3) data03 on ");
            for (ModalField modalField : dimList) {
                String dbCode = modalField.getDbCode();
                if ("ZBTYPE".equals(dbCode) || "VERSION".equals(dbCode)) {
                    continue;
                }
                sqlBuilder.append("data01.").append(dbCode).append("=data03.").append(dbCode).append(" and ");
            }
            sqlBuilder.setLength(sqlBuilder.length() - 4);
            sqlBuilder.append(") where ");
            List<ModalField> meas = hyperModalDO.getMeas();
            for (ModalField mea : meas) {
                sqlBuilder.append("Coalesce(data01.").append(mea.getDbCode()).append(",0)<>Coalesce(data03.").append(mea.getDbCode()).append(",0)");
                sqlBuilder.append(" and ");
            }
            String sql = sqlBuilder.substring(0, sqlBuilder.length() - 4);
            List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
            if (maps.isEmpty()) {
                continue;
            }
            for (Map<String, Object> map : maps) {

            }
        }
    }
}
