package com.example.io;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class AIOClient {
    private static final Logger logger = LoggerFactory.getLogger(AIOClient.class);
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
        CountDownLatch countDownLatch = new CountDownLatch(1);
        long timeMillis = System.currentTimeMillis();
        AsynchronousSocketChannel asynchronousSocketChannel = AsynchronousSocketChannel.open();
        asynchronousSocketChannel.connect(new InetSocketAddress("localhost",8080), asynchronousSocketChannel, new CompletionHandler<Void, AsynchronousSocketChannel>() {
            @Override
            public void completed(Void result, AsynchronousSocketChannel attachment) {
                logger.info("连接建立成功，耗时{}毫秒",System.currentTimeMillis()-timeMillis);
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                byteBuffer.put(exp.getBytes(StandardCharsets.UTF_8));
                byteBuffer.flip();
                attachment.write(byteBuffer, attachment, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
                    @Override
                    public void completed(Integer result, AsynchronousSocketChannel attachment) {
                        logger.info("传入计算表达式：{}",exp);
                        byteBuffer.clear();
                        attachment.read(byteBuffer, attachment, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
                            @Override
                            public void completed(Integer result, AsynchronousSocketChannel attachment) {
                                StringBuilder builder = new StringBuilder();
                                byteBuffer.flip();
                                builder.append(StandardCharsets.UTF_8.decode(byteBuffer));
                                countDownLatch.countDown();
                                logger.info("服务器返回结果为：{}", builder);
                                try {
                                    attachment.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                                logger.error("数据读取失败",exc);
                            }
                        });
                    }

                    @Override
                    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                        logger.error("数据写入失败",exc);
                    }
                });
            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                logger.error("连接失败",exc);
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
