package com.example.rpc.server;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    public static void main(String[] args) {
        int port = 8081;
        ThreadPoolExecutor ioThreadPool = getThreadPoolExecutor(port);
        try {
            try(ServerSocket serverSocket = new ServerSocket(port)){
                logger.info("绑定端口{}",port);
                while (true){
                    Socket socket = serverSocket.accept();
                    logger.info("接收到新请求");
                    ioThreadPool.execute(()->{
                        InputStream inputStream = null;
                        try {
                            inputStream = socket.getInputStream();
                            byte[] bytes = new byte[1024];
                            int read;
                            StringBuilder builder = new StringBuilder();
                            while ((read= inputStream.read(bytes))>0){
                                String s = new String(bytes, 0, read);
                                builder.append(s);
                            }
                            logger.info("请求内容：{}",builder);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ThreadPoolExecutor getThreadPoolExecutor(int port) {
        return new ThreadPoolExecutor(10, 20, 120, TimeUnit.SECONDS, new ArrayBlockingQueue<>(20), new ThreadFactory() {
            private final AtomicInteger atomicInteger = new AtomicInteger();
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"rpc-io-"+ port +"-"+atomicInteger.getAndIncrement());
            }
        }, (r, executor) -> {
            throw new RuntimeException("thread limit!");
        });
    }
}
