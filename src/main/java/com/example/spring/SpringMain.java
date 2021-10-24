package com.example.spring;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 参数解析
 */
@SpringBootApplication(scanBasePackages = "com.example.spring")
public class SpringMain {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringMain.class, args);
//        WebSocketServer webSocketServer = (WebSocketServer) applicationContext.getBean("myWebSocketServer");
//        while (true) {
//            try {
//                TimeUnit.SECONDS.sleep(10);
//                webSocketServer.send2AllUser("222222");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }


}
