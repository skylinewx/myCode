package com.example.io;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class NIOClient {
    private static final Logger logger = LoggerFactory.getLogger(NIOClient.class);

    @Test
    public void test1() throws IOException {
        test("22+33+44+55");
    }
    @Test
    public void test2() throws IOException {
        test("22*33*44*55");
    }
    @Test
    public void test3() throws IOException {
        test("7*(8+2)");
    }

    @Test
    public void test4() throws IOException {
        test("2*30-99");
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

    private void test(String exp) throws IOException {
        logger.info("开始建立连接");
        long timeMillis = System.currentTimeMillis();
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(8080));
        boolean finishConnect;
        do {
            finishConnect = socketChannel.finishConnect();
        }while (!finishConnect);
        logger.info("连接建立成功，耗时{}毫秒",System.currentTimeMillis()-timeMillis);
        logger.info("传入计算表达式：{}",exp);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        byteBuffer.put(exp.getBytes(StandardCharsets.UTF_8));
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        byteBuffer.clear();
        int read;
        do {
            read = socketChannel.read(byteBuffer);
        }while (read==0 || read==-1);
        byteBuffer.flip();
        StringBuilder result = new StringBuilder();
        result.append(StandardCharsets.UTF_8.decode(byteBuffer));
        logger.info("服务器返回结果为：{}", result);
        byteBuffer.clear();
        socketChannel.close();
    }
}
