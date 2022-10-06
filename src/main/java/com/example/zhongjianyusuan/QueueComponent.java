package com.example.zhongjianyusuan;

import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author wangxing
 * @date 2022/9/2
 **/
@Component
public class QueueComponent {

    private final LinkedBlockingQueue<SplitTableDTO> queue = new LinkedBlockingQueue<>();

    public void pushTableName(SplitTableDTO tableName) throws InterruptedException {
        queue.put(tableName);
    }

    public SplitTableDTO takeTableName() throws InterruptedException {
        return queue.take();
    }
}
