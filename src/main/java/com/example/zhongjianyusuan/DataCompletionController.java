package com.example.zhongjianyusuan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/doDataCompletionAsyncByModal/{modalName}")
    private String doDataCompletionAsyncByModal(@PathVariable String modalName){
        dataCompletionService.doDataCompletionAsyncByModal(modalName);
        return "ok";
    }

    @GetMapping("/doDataCompletionAsyncByModals/{modalNames}")
    private String doDataCompletionAsyncByModals(@PathVariable String modalNames){
        dataCompletionService.doDataCompletionAsyncByModals(modalNames);
        return "ok";
    }
}
