package com.example.spring.problem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/test")
    private String test(){
        return testService.test();
    }

    @GetMapping("/test2")
    public String test2(){
        return testService.test2();
    }
}
