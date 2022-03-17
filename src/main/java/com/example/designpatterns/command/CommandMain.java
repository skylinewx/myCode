package com.example.designpatterns.command;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 命令模式
 */
@Configuration
@ComponentScan("com.example.designpatterns.command")
public class CommandMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CommandMain.class);
        Waiter waiter = context.getBean(Waiter.class);
        waiter.showOrder();
        waiter.beginOrder();
        waiter.addItem("baodu");
        waiter.addItem("mapodoufu");
        waiter.addItem("baiqieji");
        waiter.endOrder();
    }
}
