package com.example.designpatterns.chainofresponsibility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author wangxing
 */
@SpringBootApplication
public class SpringBootFilterApp {
    private static final Logger logger = LoggerFactory.getLogger(SpringBootFilterApp.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootFilterApp.class, args);
    }

    @RestController
    @RequestMapping("/test")
    private static class TestController {
        @GetMapping("/run")
        public String run() {
            logger.info("run");
            return "run";
        }
    }

    @Component
    private static class TestFilter1 implements Filter {

        private static final Logger logger = LoggerFactory.getLogger(TestFilter1.class);

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            //这里处理request
            logger.info("before chain.doFilter");
            chain.doFilter(request, response);
            logger.info("after chain.doFilter");
            //这里处理response
        }
    }

    @Component
    private static class TestFilter2 implements Filter {

        private static final Logger logger = LoggerFactory.getLogger(TestFilter2.class);

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            //这里处理request
            logger.info("before chain.doFilter");
            chain.doFilter(request, response);
            logger.info("after chain.doFilter");
            //这里处理response
        }
    }

    @Component
    private static class TestFilter3 implements Filter {
        private static final Logger logger = LoggerFactory.getLogger(TestFilter3.class);

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            //这里处理request
            logger.info("before chain.doFilter");
            chain.doFilter(request, response);
            logger.info("after chain.doFilter");
            //这里处理response
        }
    }
}
