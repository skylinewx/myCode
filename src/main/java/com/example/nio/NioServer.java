package com.example.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * nio服务端
 *
 * @author wangxing
 */
public class NioServer extends Thread {

    private Selector selector;

    public static void main(String[] args) {
        NioServer server = new NioServer();
        server.setName("NioServer_1");
        server.start();
    }

    @Override
    public void run() {
        //开启一个channel
        init();
        try {
            doListen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //将channel绑定到localhost:port上，使用默认的50个等待长度
            serverSocketChannel.bind(new InetSocketAddress(8080));
            //设置channel为非阻塞
            serverSocketChannel.configureBlocking(false);
            //开启一个selector
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doListen() throws IOException {
        while (true) {
            //阻塞式等待
            int select = selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                handleKey(selectionKey);
            }
        }
    }

    private void handleKey(SelectionKey selectionKey) throws IOException {
        //新连接
        if (selectionKey.isAcceptable()) {
            handleAcceptable(selectionKey);
        }
        //当有新的数据到达时
        if (selectionKey.isReadable()) {
            handleReadable(selectionKey);
        }
        //当可写时
        if (selectionKey.isWritable()) {
            handleWritable(selectionKey);
        }
    }

    private void handleWritable(SelectionKey selectionKey) throws IOException {
        System.out.println("isWritable");
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        Object attachment = selectionKey.attachment();
        String message = "hello,i'm " + getName() + " " + attachment;
        byteBuffer.put(message.getBytes());
        byteBuffer.flip();
        channel.write(byteBuffer);
        channel.register(selector, SelectionKey.OP_READ);
    }

    private void handleReadable(SelectionKey selectionKey) throws IOException {
        System.out.println("isReadable");
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer readBuffer = ByteBuffer.allocateDirect(16);
        int read;
        StringBuilder receiveData = new StringBuilder();
        //将所有可读的数据一次性读完，其实这里不写while也可以，只是会进来两次
        while ((read = socketChannel.read(readBuffer)) > 0) {
            readBuffer.flip();
            //使用GBK是因为我本地是使用telnet测试的
//                receiveData = receiveData + Charset.forName("GBK").decode(readBuffer);
            receiveData.append(StandardCharsets.UTF_8.decode(readBuffer));
            readBuffer.clear();
        }
        System.out.println("receiveData:" + receiveData);
        selectionKey.attach(receiveData);
        if (read < 0) {
            //资源关闭，当telnet执行q命令时触发
            selectionKey.cancel();
            socketChannel.close();
        } else {
            socketChannel.register(selector, SelectionKey.OP_WRITE);
        }
    }

    private void handleAcceptable(SelectionKey selectionKey) throws IOException {
        System.out.println("isAcceptable");
        //获取到新连接的channel
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        //新的channel也设置成非阻塞的
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        //1kb=1024b=1024byte
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("new channel!".getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
    }
}
