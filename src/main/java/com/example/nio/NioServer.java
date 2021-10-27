package com.example.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * nio服务端
 *
 * @author wangxing
 */
public class NioServer extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(NioServer.class);
    private Selector selector;
    private Map<Channel, ChannelHandler> channelHandlerMap;

    public static void main(String[] args) {
        NioServer server = new NioServer();
        server.setName("NioServer_1");
        server.start();
    }

    @Override
    public void run() {
        //开启一个channel
        init();
        doListen();
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
            channelHandlerMap = new HashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doListen() {
        while (true) {
            //阻塞式等待
            try {
                int select = selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                try {
                    handleKey(selectionKey);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    private void handleWritable(SelectionKey selectionKey) {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        logger.info("writable [{}] ", channel);
        channelHandlerMap.get(channel).doChannelWritable();

    }

    private void handleReadable(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        logger.info("readable [{}] ", socketChannel);
        boolean readSuccess = channelHandlerMap.get(socketChannel).doChannelRead();
        if (!readSuccess) {
            //资源关闭，当telnet执行q命令时触发
            selectionKey.cancel();
            socketChannel.close();
        }
    }

    private void handleAcceptable(SelectionKey selectionKey) throws IOException {
        //获取到新连接的channel
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        ServerChannelHandler serverChannelHandler = new ServerChannelHandler(socketChannel, selector);
        //注册一个新的channelHandler
        channelHandlerMap.put(socketChannel, serverChannelHandler);
        logger.info("new acceptable channel");
    }
}
