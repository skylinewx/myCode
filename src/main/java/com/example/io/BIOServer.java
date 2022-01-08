package com.example.io;

import com.googlecode.aviator.AviatorEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * BIO模型，同步阻塞<br/>
 * 优点：没有空转，不浪费cpu<br/>
 * 缺点：每个请求都需要使用独立的线程来处理，业务服务吞吐量和线程池大小相关<br/>
 * @author skyline
 */
public class BIOServer {
    private static final Logger logger = LoggerFactory.getLogger(BIOServer.class);

    public static void main(String[] args) throws IOException {
        ThreadPoolExecutor bizThreadPoolExecutor = createBizThreadPool();
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true) {
            logger.info("等待新连接");
            //这里用while(true)来接收请求，只要业务处理线程池还吃得下，就可以一直接收请求
            //由于是使用线程池处理请求的具体内容，所以就算是连接上了，也不一定能like处理
            Socket accept = serverSocket.accept();
            logger.info("获取到连接了{}", accept);
            bizThreadPoolExecutor.execute(() -> {
                handleSocket(accept);
            });
        }
    }

    private static ThreadPoolExecutor createBizThreadPool() {
        return new ThreadPoolExecutor(3, 3, 0, TimeUnit.SECONDS, new SynchronousQueue<>(),
                new ThreadFactory() {
                    private final AtomicInteger atomicInteger = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "BIOServer-" + atomicInteger.getAndIncrement());
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

    private static void handleSocket(Socket socket) {
        logger.info("业务线程处理连接,{}", socket);
        InputStream inputStream = null;
        OutputStream out = null;
        try {
            StringBuilder builder = new StringBuilder();
            inputStream = socket.getInputStream();
            out = socket.getOutputStream();
            byte[] bytes = new byte[1024];
            //同步阻塞读取，如果没有可读的，就会在这里阻塞
            //如果是需要有返回值的请求，那这里就不能while(read!=-1)，必须一次性读取
            //如果while(read!=-1)，当客户端的socket不关闭时，服务端会一直阻塞在inputStream.read(bytes)
            //所以bytes的大小比较关键
            int read = inputStream.read(bytes);
            if(read==-1){
                return;
            }
            builder.append(new String(bytes, 0, read));
            logger.info("读取到来自客户端的数据[{}]", builder);
            Object result;
            try {
                result = AviatorEvaluator.execute(builder.toString());
                logger.info("AviatorEvaluator的计算结果是[{}]", result);
            } catch (Exception e) {
                result = e.getMessage();
            }
            out.write(result.toString().getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
