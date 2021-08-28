package com.example.spring.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@Configuration
public class AspectConfig {

    @Bean
    public MyAspect1 getMyAspect1() {
        return new MyAspect1();
    }
}
