package com.example.zhongjianyusuan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataCompletionController {

    @Autowired
    private BizDataCompletionService dataCompletionService;


    @GetMapping("/doDataCompletionAsync")
    private String doDataCompletion(){
        dataCompletionService.doDataCompletion();
        return "ok";
    }
}
