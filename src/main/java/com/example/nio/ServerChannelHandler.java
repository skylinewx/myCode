package com.example.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 服务端channel处理器
 *
 * @author wangxing
 */
public class ServerChannelHandler extends ChannelHandler {

    public ServerChannelHandler(SelectableChannel socketChannel, Selector selector) throws IOException {
        super(socketChannel, selector);
    }

    @Override
    public boolean doChannelRead() {
        try {
            ByteBuffer readBuffer = ByteBuffer.allocateDirect(16);
            int read;
            StringBuilder receiveData = new StringBuilder();
            //将所有可读的数据一次性读完，其实这里不写while也可以，只是会进来两次
            while ((read = ((SocketChannel) socketChannel).read(readBuffer)) > 0) {
                readBuffer.flip();
                receiveData.append(StandardCharsets.UTF_8.decode(readBuffer));
                readBuffer.clear();
            }
            if (read < 0) {
                return false;
            } else {
                socketChannel.register(selector, SelectionKey.OP_WRITE);
            }
            logger.info("receiveData:{}", receiveData);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return true;
    }

    @Override
    public void doChannelWritable() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        String message = "hello,i'm xxx";
        byteBuffer.put(message.getBytes());
        byteBuffer.flip();
        Random random = new Random();
        int nextInt = random.nextInt(10);
        logger.info("sleep [{}] s", nextInt);
        try {
            TimeUnit.SECONDS.sleep(nextInt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("write message [{}]", message);
        try {
            ((SocketChannel) socketChannel).write(byteBuffer);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
