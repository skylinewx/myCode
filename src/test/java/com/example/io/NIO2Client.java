package com.example.io;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class NIO2Client {
    private static final Logger logger = LoggerFactory.getLogger(NIO2Client.class);

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
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress(8080));
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while(true){
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isConnectable()) {
                    logger.info("连接建立成功，耗时{}毫秒",System.currentTimeMillis()-timeMillis);
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    channel.configureBlocking(false);
                    channel.finishConnect();
                    channel.register(selector, SelectionKey.OP_WRITE);
                }else if(selectionKey.isWritable()){
                    logger.info("传入计算表达式：{}",exp);
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    byteBuffer.put(exp.getBytes(StandardCharsets.UTF_8));
                    byteBuffer.flip();
                    channel.write(byteBuffer);
                    byteBuffer.clear();
                    channel.register(selector, SelectionKey.OP_READ);
                }else if(selectionKey.isReadable()){
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    int read = channel.read(byteBuffer);
                    byteBuffer.flip();
                    StringBuilder builder = new StringBuilder();
                    builder.append(StandardCharsets.UTF_8.decode(byteBuffer));
                    logger.info("服务器返回结果为：{}", builder);
                    channel.close();
                    return;
                }
            }
        }
    }
}
