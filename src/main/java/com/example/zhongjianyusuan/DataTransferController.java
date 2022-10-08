package com.example.zhongjianyusuan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangxing
 * @date 2022/10/8
 **/
@RestController
public class DataTransferController {

    @Autowired
    private DataTransferService transferService;

    @GetMapping("/doDataTransfer1/{srcTable}/{tarTable}")
    private String doDataTransfer1(@PathVariable String srcTable, @PathVariable String tarTable) {
        Thread thread = new Thread(() -> {
            transferService.tryTransferData(srcTable, tarTable);
        }, "ttt");
        thread.start();
        return "ok";
    }

    @GetMapping("/doDataTransfer2/{srcTable}/{tarTable}")
    private String doDataTransfer2(@PathVariable String srcTable, @PathVariable String tarTable) {
        Thread thread = new Thread(() -> {
            transferService.tryTransferData2(srcTable, tarTable);
        }, "ttt");
        thread.start();
        return "ok";
    }
}
