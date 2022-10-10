package com.example.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.example.data")
@EnableAsync
public class SpringMain {

    private static final Logger logger = LoggerFactory.getLogger(SpringMain.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringMain.class, args);
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
//        ThreadPoolExecutor myThreadPool = applicationContext.getBean("MyThreadPool", ThreadPoolExecutor.class);
//        List<String> orgList = jdbcTemplate.queryForList(" select code from md_org where code<>'GT00'", String.class);
//        String sql = "insert into %s(mdcode,datatime,md_scene,md_currency,md_mgrver,md_stage,bizkeyorder,floatorder,zb_je2,md_cwsdbwd) select '%s' as mdcode,datatime,md_scene,md_currency,md_mgrver,md_stage,sys_guid() as bizkeyorder,floatorder,zb_je2,md_cwsdbwd from %s where mdcode='GT00'";
//        int size = orgList.size();
//        for (int i = 0; i < size; i++) {
//            String org = orgList.get(i);
//            String format = String.format(sql, "wx1_Y", org, "wx1_Y");
//            int finalI = i;
//            myThreadPool.submit(() -> {
//                LocalDateTime begin = LocalDateTime.now();
//                jdbcTemplate.execute(format);
//                logger.info("[{}/{}] finish,time:[{}]", finalI + 1, size, Duration.between(begin, LocalDateTime.now()));
//            });
//        }
        DataCopyService dataCopyService = applicationContext.getBean(DataCopyService.class);
        dataCopyService.doCopy("WX1_Y");
        logger.info("启动完毕");
    }


}