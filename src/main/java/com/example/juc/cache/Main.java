package com.example.juc.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.example.juc.cache")
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args);
//        StudentService studentService = applicationContext.getBean(StudentService.class);
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 120, TimeUnit.SECONDS, new SynchronousQueue<>(), new ThreadFactory() {
//            private final AtomicInteger integer = new AtomicInteger(1);
//
//            @Override
//            public Thread newThread(Runnable r) {
//                return new Thread(r, "cache-test-" + integer.getAndIncrement());
//            }
//        }, (r, executor) -> {
//            logger.info("线程池已满");
//            try {
//                executor.getQueue().put(r);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            logger.info("线程池有位置了");
//        });
//
//        int maxCount = 100;
//        logger.info("开始");
//        LocalDateTime start = LocalDateTime.now();
//        for (int i = 0; i < maxCount; i++) {
//            int finalI = i;
//            threadPoolExecutor.submit(() -> {
//                StudentDO student = studentService.getByIdFromHashMapCache("001");
//                logger.info("第[{}]次查询结束,查询结果为[{}]", finalI, student);
//            });
//        }
//        threadPoolExecutor.shutdown();
//        logger.info("结束[{}]", Duration.between(start, LocalDateTime.now()));


    }
}
