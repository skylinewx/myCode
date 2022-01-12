package com.example.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 异步IO服务，仅在Windows服务器下有意义<br/>
 * 只有win实现了真正的AIO，Unix上都是使用epoll实现的AIO，效果并不好
 *
 * @author skyline
 */
public class AIOServer implements IServer{
    private static final Logger logger = LoggerFactory.getLogger(AIOServer.class);

    public static void main(String[] args) throws IOException {
        AIOServer server = new AIOServer();
        server.start(new NullProtocolHandler(), 8080);
    }

    @Override
    public void start(BaseProtocolHandler protocolHandler, int port) throws IOException {
        AsynchronousServerSocketChannel serverSocketChannel = init(port);
        listen(protocolHandler,serverSocketChannel);
        try {
            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private AsynchronousServerSocketChannel init(int port) throws IOException {
        ThreadPoolExecutor bizThreadPool = createBizThreadPool();
        AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withThreadPool(bizThreadPool);
        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
        serverSocketChannel.bind(new InetSocketAddress(port));
        return serverSocketChannel;
    }

    void listen(BaseProtocolHandler protocolHandler, AsynchronousServerSocketChannel serverSocketChannel) throws IOException {
        logger.info("等待新连接");
        serverSocketChannel.accept(this, new CompletionHandler<AsynchronousSocketChannel, AIOServer>() {
            @Override
            public void completed(AsynchronousSocketChannel socketChannel, AIOServer attachment) {
                if (socketChannel.isOpen()) {
                    logger.info("获取到连接了{}", socketChannel);
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    socketChannel.read(byteBuffer, socketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
                        @Override
                        public void completed(Integer result, AsynchronousSocketChannel channel) {
                            byteBuffer.flip();
                            StringBuilder builder = new StringBuilder();
                            builder.append(StandardCharsets.UTF_8.decode(byteBuffer));
                            byteBuffer.clear();
                            logger.info("读取到来自客户端的数据[{}]", builder);
                            byte[] calcResult = protocolHandler.handleRequest(builder.toString());
                            byteBuffer.put(calcResult);
                            byteBuffer.flip();
                            channel.write(byteBuffer, channel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
                                @Override
                                public void completed(Integer result, AsynchronousSocketChannel attachment) {
                                    logger.info("数据写入完毕{}", attachment);
                                    byteBuffer.clear();
                                    try {
                                        attachment.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                                    logger.error("write failed",exc);
                                }
                            });
                        }

                        @Override
                        public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                            logger.error("read failed",exc);
                        }
                    });
                }
                try {
                    listen(protocolHandler, serverSocketChannel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, AIOServer attachment) {
                logger.error("accept failed",exc);
            }
        });
    }

    private ThreadPoolExecutor createBizThreadPool() {
        return new ThreadPoolExecutor(3, 3, 0, TimeUnit.SECONDS, new SynchronousQueue<>(),
                new ThreadFactory() {
                    private final AtomicInteger atomicInteger = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "AIOServer-" + atomicInteger.getAndIncrement());
                    }
                },
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        logger.info("当前没有可用的线程了，任务被挂起");
                        try {
                            executor.getQueue().put(r);
                            logger.info("任务成功入队");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
