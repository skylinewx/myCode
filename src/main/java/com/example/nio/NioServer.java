package com.example.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * nio服务端
 *
 * @author wangxing
 */
public class NioServer {

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public static void main(String[] args) throws IOException {
        NioServer server = new NioServer();
        server.start(8080);
        server.doListen();
    }

    /**
     * 启动服务
     *
     * @param port 端口号
     * @throws IOException
     */
    public void start(int port) throws IOException {
        //开启一个channel
        serverSocketChannel = ServerSocketChannel.open();
        //将channel绑定到localhost:port上，使用默认的50个等待长度
        serverSocketChannel.bind(new InetSocketAddress(port));
        //设置channel为非阻塞
        serverSocketChannel.configureBlocking(false);
        //开启一个selector
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
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
                //新连接
                if (selectionKey.isAcceptable()) {
                    System.out.println("isAcceptable");
                    //获取到新连接的channel
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    if (serverSocketChannel == null) {
                        continue;
                    }
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
                //当有新的数据到达时
                if (selectionKey.isReadable()) {
                    System.out.println("isReadable");
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocateDirect(1024);
                    int read = socketChannel.read(readBuffer);
                    if (read > 0) {
                        readBuffer.flip();
                        //使用GBK是因为我本地是使用telnet测试的
                        String receiveData = Charset.forName("GBK").decode(readBuffer).toString();
                        System.out.println("receiveData:" + receiveData);
                    } else if (read == 0) {

                    } else {
                        //资源关闭，当telnet执行q命令时触发
                        selectionKey.cancel();
                        socketChannel.close();
                    }

                }
            }
        }
    }
}
