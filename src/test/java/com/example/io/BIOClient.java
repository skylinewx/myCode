package com.example.io;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BIOClient {

    private static final Logger logger = LoggerFactory.getLogger(BIOClient.class);

    @Test
    public void test1() throws IOException {
        testBIOClient("22+33+44+55");
    }
    @Test
    public void test2() throws IOException {
        testBIOClient("22*33*44*55");
    }
    @Test
    public void test3() throws IOException {
        testBIOClient("7*(8+2)");
    }

    @Test
    public void test4() throws IOException {
        testBIOClient("2*30-99");
    }

    @Test
    public void test5() throws IOException, InterruptedException {
        int count=10;
        List<Thread> threads = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Thread thread = new Thread(() -> {
                try {
                    test1();
                    test2();
                    test3();
                    test4();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }

    private void testBIOClient(String exp) throws IOException {
        logger.info("开始建立连接");
        long timeMillis = System.currentTimeMillis();
        Socket socket =new Socket("localhost",8080);
        logger.info("连接建立成功，耗时{}毫秒",System.currentTimeMillis()-timeMillis);
        OutputStream outputStream = socket.getOutputStream();
        logger.info("传入计算表达式：{}",exp);
        outputStream.write(exp.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        InputStream inputStream = socket.getInputStream();
        int read;
        byte[] bytes = new byte[1024];
        StringBuilder builder = new StringBuilder();
        while((read=inputStream.read(bytes))!=-1){
            builder.append(new String(bytes,0,read));
        }
        logger.info("服务器返回结果为：{}", builder);
        inputStream.close();
        outputStream.close();
        socket.close();
    }
}
