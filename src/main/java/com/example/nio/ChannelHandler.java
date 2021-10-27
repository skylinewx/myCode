package com.example.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * channel处理器
 *
 * @author wangxing
 */
public abstract class ChannelHandler {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected SelectableChannel socketChannel;
    protected Selector selector;

    ChannelHandler(SelectableChannel socketChannel, Selector selector) throws IOException {
        //新的channel也设置成非阻塞的
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        this.socketChannel = socketChannel;
        this.selector = selector;
    }

    /**
     * 当channel可读时,执行读的逻辑
     */
    public abstract boolean doChannelRead();

    /**
     * 当channel可写时,执行写入的逻辑
     */
    public abstract void doChannelWritable();
}
