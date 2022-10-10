package com.example.jmh;

import com.example.zhongjianyusuan.SpringMain;
import oracle.sql.TIMESTAMP;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

/**
 * @author wangxing
 * @date 2022/10/9
 **/
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(time = 1, iterations = 2)
@Measurement(time = 10, iterations = 5)
@Fork(1)
@State(Scope.Benchmark)
public class PreparedStatementSetValueTest {

    private static final Logger logger = LoggerFactory.getLogger(PreparedStatementSetValueTest.class);

    private ConfigurableApplicationContext applicationContext;
    private JdbcTemplate jdbcTemplate;
    private String insertSql;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(PreparedStatementSetValueTest.class.getSimpleName())
                .result("PreparedStatementSetValueTest.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }

    @Setup
    public void init() {
        applicationContext = SpringApplication.run(SpringMain.class);
        jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
        jdbcTemplate.execute("truncate table wx_test");
        insertSql = "insert into WX_TEST(\"field1\", \"field2\", \"field3\", \"field4\", \"field5\", \"field6\", \"field7\", \"field8\", \"field9\", \"field10\",\n" +
                "                    \"field11\", \"field12\", \"field13\", \"field14\", \"field15\", \"field16\", \"field17\", \"field18\", \"field19\",\n" +
                "                    \"field20\", \"field21\", \"field22\", \"field23\", \"field24\", \"field25\", \"field26\", \"field27\", \"field28\",\n" +
                "                    \"field29\", \"field30\", \"field31\", \"field32\", \"field33\", \"field34\", \"field35\", \"field36\", \"field37\",\n" +
                "                    \"field38\", \"field39\", \"field40\", \"field41\", \"field42\", \"field43\", \"field44\", \"field45\", \"field46\",\n" +
                "                    \"field47\", \"field48\", \"field49\", \"field50\", \"field51\", \"field52\", \"field53\", \"field54\", \"field55\",\n" +
                "                    \"field56\", \"field57\", \"field58\", \"field59\", \"field60\", \"field61\", \"field62\", \"field63\", \"field64\",\n" +
                "                    \"field65\", \"field66\", \"field67\", \"field68\", \"field69\", \"field70\", \"field71\", \"field72\", \"field73\",\n" +
                "                    \"field74\", \"field75\", \"field76\", \"field77\", \"field78\", \"field79\", \"field80\", \"field81\", \"field82\",\n" +
                "                    \"field83\", \"field84\", \"field85\", \"field86\")\n" +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,\n" +
                "        ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,\n" +
                "        ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @TearDown
    public void down() {
        Integer totalCount = jdbcTemplate.queryForObject("select count(1) from wx_test", Integer.class);
        logger.info("插入测试完毕，总插入量{}", totalCount);
//        System.out.println("totalCount = " + totalCount);
        applicationContext.close();
    }

    private byte[] getBytes(String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }

    @Benchmark
    public void testSetObject() throws InterruptedException {
        jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, getBytes("field1"));
                ps.setObject(2, 111111);
                ps.setObject(3, getBytes("field3"));
                ps.setObject(4, "field4");
                ps.setObject(5, 5467.45);
                ps.setObject(6, 32);
                ps.setObject(7, 56);
                ps.setObject(8, getBytes("field8"));
                ps.setObject(9, null);
                ps.setObject(10, null);
                ps.setObject(11, null);
                ps.setObject(12, new TIMESTAMP());
                ps.setObject(13, new TIMESTAMP());
                ps.setObject(14, new BigDecimal("23.45"));
                ps.setObject(15, null);
                ps.setObject(16, null);
                ps.setObject(17, null);
                ps.setObject(18, null);
                ps.setObject(19, new BigDecimal("8878.4"));
                ps.setObject(20, null);
                ps.setObject(21, new BigDecimal("45.78"));
                ps.setObject(22, null);
                ps.setObject(23, null);
                ps.setObject(24, null);
                ps.setObject(25, new BigDecimal("87.487"));
                ps.setObject(26, null);
                ps.setObject(27, null);
                ps.setObject(28, null);
                ps.setObject(29, new BigDecimal("88.47"));
                ps.setObject(30, null);
                ps.setObject(31, null);
                ps.setObject(32, new BigDecimal("12.45"));
                ps.setObject(33, null);
                ps.setObject(34, null);
                ps.setObject(35, null);
                ps.setObject(36, null);
                ps.setObject(37, null);
                ps.setObject(38, null);
                ps.setObject(39, new BigDecimal("45.48"));
                ps.setObject(40, null);
                ps.setObject(41, null);
                ps.setObject(42, null);
                ps.setObject(43, null);
                ps.setObject(44, null);
                ps.setObject(45, null);
                ps.setObject(46, new BigDecimal("45434.74"));
                ps.setObject(47, null);
                ps.setObject(48, null);
                ps.setObject(49, null);
                ps.setObject(50, null);
                ps.setObject(51, getBytes("field51"));
                ps.setObject(52, getBytes("field52"));
                ps.setObject(53, null);
                ps.setObject(54, null);
                ps.setObject(55, null);
                ps.setObject(56, null);
                ps.setObject(57, null);
                ps.setObject(58, new BigDecimal("5453.5"));
                ps.setObject(59, null);
                ps.setObject(60, null);
                ps.setObject(61, null);
                ps.setObject(62, null);
                ps.setObject(63, null);
                ps.setObject(64, null);
                ps.setObject(65, new BigDecimal("453.7487"));
                ps.setObject(66, null);
                ps.setObject(67, null);
                ps.setObject(68, null);
                ps.setObject(69, new BigDecimal("78534.1"));
                ps.setObject(70, null);
                ps.setObject(71, null);
                ps.setObject(72, new BigDecimal("5687.78"));
                ps.setObject(73, null);
                ps.setObject(74, new BigDecimal("543"));
                ps.setObject(75, null);
                ps.setObject(76, null);
                ps.setObject(77, new BigDecimal("124.45"));
                ps.setObject(78, null);
                ps.setObject(79, null);
                ps.setObject(80, new BigDecimal("5424"));
                ps.setObject(81, null);
                ps.setObject(82, new BigDecimal("4537.75"));
                ps.setObject(83, null);
                ps.setObject(84, null);
                ps.setObject(85, null);
                ps.setObject(86, null);
            }

            @Override
            public int getBatchSize() {
                return 1000;
            }
        });
    }

    @Benchmark
    public void testSetValue() throws InterruptedException {
        jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setBytes(1, getBytes("field1"));
                ps.setInt(2, 111111);
                ps.setBytes(3, getBytes("field3"));
                ps.setString(4, "field4");
                ps.setBigDecimal(5, new BigDecimal("5467.45"));
                ps.setInt(6, 32);
                ps.setInt(7, 56);
                ps.setBytes(8, getBytes("field8"));
                ps.setString(9, null);
                ps.setBigDecimal(10, null);
                ps.setBigDecimal(11, null);
                ps.setTimestamp(12, new Timestamp(System.currentTimeMillis()));
                ps.setTimestamp(13, new Timestamp(System.currentTimeMillis()));
                ps.setBigDecimal(14, new BigDecimal("23.45"));
                ps.setBigDecimal(15, null);
                ps.setBigDecimal(16, null);
                ps.setBigDecimal(17, null);
                ps.setBigDecimal(18, null);
                ps.setBigDecimal(19, new BigDecimal("8878.4"));
                ps.setBigDecimal(20, null);
                ps.setBigDecimal(21, new BigDecimal("45.78"));
                ps.setBigDecimal(22, null);
                ps.setBigDecimal(23, null);
                ps.setBigDecimal(24, null);
                ps.setBigDecimal(25, new BigDecimal("87.487"));
                ps.setBigDecimal(26, null);
                ps.setBigDecimal(27, null);
                ps.setBigDecimal(28, null);
                ps.setBigDecimal(29, new BigDecimal("88.47"));
                ps.setBigDecimal(30, null);
                ps.setBigDecimal(31, null);
                ps.setBigDecimal(32, new BigDecimal("12.45"));
                ps.setBigDecimal(33, null);
                ps.setBigDecimal(34, null);
                ps.setBigDecimal(35, null);
                ps.setBigDecimal(36, null);
                ps.setBigDecimal(37, null);
                ps.setBigDecimal(38, null);
                ps.setBigDecimal(39, new BigDecimal("45.48"));
                ps.setBigDecimal(40, null);
                ps.setBigDecimal(41, null);
                ps.setBigDecimal(42, null);
                ps.setBigDecimal(43, null);
                ps.setBigDecimal(44, null);
                ps.setBigDecimal(45, null);
                ps.setBigDecimal(46, new BigDecimal("45434.74"));
                ps.setBigDecimal(47, null);
                ps.setBigDecimal(48, null);
                ps.setBigDecimal(49, null);
                ps.setString(50, null);
                ps.setBytes(51, getBytes("field51"));
                ps.setBytes(52, getBytes("field52"));
                ps.setTimestamp(53, null);
                ps.setBigDecimal(54, null);
                ps.setTimestamp(55, null);
                ps.setBigDecimal(56, null);
                ps.setBigDecimal(57, null);
                ps.setBigDecimal(58, new BigDecimal("5453.5"));
                ps.setBigDecimal(59, null);
                ps.setBigDecimal(60, null);
                ps.setBigDecimal(61, null);
                ps.setBigDecimal(62, null);
                ps.setBigDecimal(63, null);
                ps.setBigDecimal(64, null);
                ps.setBigDecimal(65, new BigDecimal("453.7487"));
                ps.setBigDecimal(66, null);
                ps.setBigDecimal(67, null);
                ps.setBigDecimal(68, null);
                ps.setBigDecimal(69, new BigDecimal("78534.1"));
                ps.setBigDecimal(70, null);
                ps.setBigDecimal(71, null);
                ps.setBigDecimal(72, new BigDecimal("5687.78"));
                ps.setBigDecimal(73, null);
                ps.setBigDecimal(74, new BigDecimal("543"));
                ps.setBigDecimal(75, null);
                ps.setBigDecimal(76, null);
                ps.setBigDecimal(77, new BigDecimal("124.45"));
                ps.setBigDecimal(78, null);
                ps.setBigDecimal(79, null);
                ps.setBigDecimal(80, new BigDecimal("5424"));
                ps.setBigDecimal(81, null);
                ps.setBigDecimal(82, new BigDecimal("4537.75"));
                ps.setBigDecimal(83, null);
                ps.setBigDecimal(84, null);
                ps.setBigDecimal(85, null);
                ps.setBigDecimal(86, null);
            }

            @Override
            public int getBatchSize() {
                return 1000;
            }
        });
    }
}
