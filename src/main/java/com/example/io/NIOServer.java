package com.example.io;

import com.googlecode.aviator.AviatorEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * NIO服务，同步非阻塞<br/>
 * 由于是非阻塞，所以程序中很多地方都需要while(true)<br/>
 * 优点：非阻塞，当客户端连接创建之后，如果还没发送数据，这时服务端不会卡住<br/>
 * 缺点：非阻塞，到处都是while(true)，性能上得不偿失
 * @author skyline
 */
public class NIOServer {
    private static final Logger logger = LoggerFactory.getLogger(NIOServer.class);

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        //设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);
        List<SocketChannel> socketChannels = new LinkedList<>();
        while (true) {
            logger.info("等待新连接");
            SocketChannel accept = serverSocketChannel.accept();
            if (accept == null) {
                //如果当前没有新连接，那就等1秒再看看
                logger.info("没有新的连接，等1秒再看看");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            logger.info("获得新连接，{}", accept);
            socketChannels.add(accept);
            Iterator<SocketChannel> iterator = socketChannels.iterator();
            while (iterator.hasNext()){
                SocketChannel socketChannel = iterator.next();
                boolean finish = handleSocket(socketChannel);
                if (finish) {
                    iterator.remove();
                }
            }
        }
    }

    private static boolean handleSocket(SocketChannel socketChannel) {
        logger.info("业务线程处理连接，{}", socketChannel);
        boolean finish = false;
        try {
            //手动设置为非阻塞模式
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        StringBuilder builder = new StringBuilder();
        try {
            //因为设置了非阻塞模式，所以read这里并不会阻塞，但是如果没有数据会返回0
            int read=socketChannel.read(byteBuffer);
            //在没有数据时，不进行下一步处理
            if (read==0 || read==-1) {
                return finish;
            }
            byteBuffer.flip();
            builder.append(StandardCharsets.UTF_8.decode(byteBuffer));
            byteBuffer.clear();
            logger.info("读取到来自客户端的数据[{}]", builder);
            Object result;
            try {
                result = AviatorEvaluator.execute(builder.toString());
                logger.info("AviatorEvaluator的计算结果是[{}]", result);
            } catch (Exception e) {
                result = e.getMessage();
            }
            byteBuffer.put(result.toString().getBytes(StandardCharsets.UTF_8));
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            finish = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (finish) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return finish;
    }

    private static ThreadPoolExecutor createBizThreadPool() {
        return new ThreadPoolExecutor(3, 3, 0, TimeUnit.SECONDS, new SynchronousQueue<>(),
                new ThreadFactory() {
                    private final AtomicInteger atomicInteger = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "NIOServer-" + atomicInteger.getAndIncrement());
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
