package com.example.spring.beans;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Bean3 implements InitializingBean {

    @Autowired
    private Bean2 bean2;

    public void hello(){
        System.out.println("hello i'm bean3 ");
        bean2.hello();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("i'm bean3's afterPropertiesSet=======");
    }
}
