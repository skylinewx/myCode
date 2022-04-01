package com.example.spring.problem2;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author wangxing
 * @date 2022/4/1
 **/
public class CircularDependencyMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.example.spring.problem2");
        BeanA beanA = applicationContext.getBean(BeanA.class);
        BeanB beanB = applicationContext.getBean(BeanB.class);
        System.out.println("(beanA.getBeanB()==beanB) = " + (beanA.getBeanB() == beanB));
        BeanA beanA1 = beanA.getBeanB().getBeanA();
        System.out.println("(beanA==beanA1) = " + (beanA == beanA1));
    }
}
