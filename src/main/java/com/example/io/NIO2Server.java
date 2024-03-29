package com.example.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * IO多路复用Server，单reactor模型
 *
 * @author skyline
 */
public class NIO2Server implements IServer{
    private static final Logger logger = LoggerFactory.getLogger(NIO2Server.class);

    public static void main(String[] args) throws IOException {
        NIO2Server server = new NIO2Server();
        server.start(new NullProtocolHandler(), 8080);
    }

    @Override
    public void start(BaseProtocolHandler protocolHandler, int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        serverSocketChannel.bind(new InetSocketAddress(port));
        logger.info("等待新连接");
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        Map<SocketChannel, byte[]> resultMap = new HashMap<>();
        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocket = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel newSocket = serverSocket.accept();
                    newSocket.configureBlocking(false);
                    newSocket.register(selector, SelectionKey.OP_READ);
                    logger.info("获取到连接了{}", newSocket);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    logger.info("有可读的连接了{}", socketChannel);
                    StringBuilder builder = new StringBuilder();
                    int read;
                    do {
                        read = socketChannel.read(byteBuffer);
                    } while (read == 0 || read == -1);
                    byteBuffer.flip();
                    builder.append(StandardCharsets.UTF_8.decode(byteBuffer));
                    byteBuffer.clear();
                    logger.info("读取到来自客户端的数据[{}]", builder);
                    byte[] result = protocolHandler.handleRequest(builder.toString());
                    resultMap.put(socketChannel, result);
                    socketChannel.register(selector, SelectionKey.OP_WRITE);
                } else if (selectionKey.isWritable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    logger.info("有可写的连接了{}", socketChannel);
                    byte[] result = resultMap.remove(socketChannel);
                    if (result != null) {
                        byteBuffer.put(result);
                        byteBuffer.flip();
                        socketChannel.write(byteBuffer);
                        byteBuffer.clear();
                    }
                    socketChannel.close();
                }
                iterator.remove();
            }
        }
    }
}
