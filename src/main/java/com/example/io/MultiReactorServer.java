package com.example.io;

import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Multi Reactor
 * @author wangxing
 */
public class MultiReactorServer {
    public static void main(String[] args) {
        int port = 8080;
        ThreadPoolExecutor readWriteThreadPool = getReadSocketThreadPool(port);
        //mainSelector负责接收链接
        try(Selector mainSelector = Selector.open();
            ServerSocketChannel mainServerSocketChannel = ServerSocketChannel.open();) {
            mainServerSocketChannel.configureBlocking(false);
            mainServerSocketChannel.bind(new InetSocketAddress(port));
            mainServerSocketChannel.register(mainSelector, SelectionKey.OP_ACCEPT);
            while (true){
                //select最大阻塞5秒
                mainSelector.select(5000);
                Set<SelectionKey> selectionKeys = mainSelector.keys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isAcceptable()) {
                        readWriteThreadPool.submit(()->{

                        });
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ThreadPoolExecutor getReadSocketThreadPool(int port) {
        ThreadPoolExecutor readWriteThreadPool = new ThreadPoolExecutor(2, 2, 120, TimeUnit.SECONDS,new SynchronousQueue<>(),new ThreadFactory(){
            private final AtomicInteger atomicInteger = new AtomicInteger();
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"rw-"+ port +"-"+atomicInteger.getAndIncrement());
            }
        }, (r, executor) -> {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return readWriteThreadPool;
    }
}
