package com.example.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author wangxing
 */
public class NioClient extends Thread {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);
    private Selector selector;

    public static void main(String[] args) {
        for (int i = 1; i < 11; i++) {
            NioClient client = new NioClient();
            client.setName("client_" + i);
            client.start();
        }
        countDownLatch.countDown();
    }

    @Override
    public void run() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        init();
        try {
            doListen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doListen() throws IOException {
        while (true) {
            int select = selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                //可以连接
                if (selectionKey.isConnectable()) {
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    //完成连接
                    if (channel.isConnectionPending()) {
                        channel.finishConnect();
                        System.out.println("连接成功");
                        String message = "hello,i'm " + getName();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
                        byteBuffer.put(message.getBytes());
                        byteBuffer.flip();
                        channel.write(byteBuffer);
                        channel.register(selector, SelectionKey.OP_READ);
                    }
                }
                if (selectionKey.isReadable()) {
                    try {
                        int sleepTime = new Random().nextInt(10);
                        System.out.println("sleep:" + sleepTime);
                        TimeUnit.SECONDS.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocateDirect(16);
                    int read;
                    StringBuilder receiveData = new StringBuilder();
                    //将所有可读的数据一次性读完，其实这里不写while也可以，只是会进来两次
                    while ((read = channel.read(readBuffer)) > 0) {
                        readBuffer.flip();
                        //使用GBK是因为我本地是使用telnet测试的
//                receiveData = receiveData + Charset.forName("GBK").decode(readBuffer);
                        receiveData.append(StandardCharsets.UTF_8.decode(readBuffer));
                        readBuffer.clear();
                    }
                    System.out.println("receiveData:" + receiveData);
                    if (read < 0) {
                        //资源关闭，当telnet执行q命令时触发
                        selectionKey.cancel();
                        channel.close();
                    } else {
                        channel.register(selector, SelectionKey.OP_WRITE);
                    }
                }
                if (selectionKey.isWritable()) {
                    System.out.println("isWritable");
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                    String message = "hello,i'm " + getName();
                    byteBuffer.put(message.getBytes());
                    byteBuffer.flip();
                    channel.write(byteBuffer);
                    channel.register(selector, SelectionKey.OP_READ);
                }
            }
        }
    }

    private void init() {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            socketChannel.connect(new InetSocketAddress(8080));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
