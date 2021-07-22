package com.example.spring;

import com.example.spring.beans.Student;
import com.example.spring.customization.MyCustomizationBean1;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 学习如何通过xml加载spring配置文件，以及BeanDefinitionRegisterPostProcessor
 */
public class SpringTests1 {

    /**
     * 通过classPath读取xml配置文件获取bean
     */
    @Test
    public void test1(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        //student是通过bean的方式手动写入的
        Object student = applicationContext.getBean("student");
        System.out.println(student);
        //teacher是通过component-scan+@Component注解结合的
        Object teacher = applicationContext.getBean("teacher");
        System.out.println(teacher);
    }

    @Test
    public void test2(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        //student是通过bean的方式手动写入的
        Student student = (Student) applicationContext.getBean("student");
        System.out.println(student);
        //teacher是通过component-scan+@Component注解结合的
        MyCustomizationBean1 myCustomizationBean1 = applicationContext.getBean(MyCustomizationBean1.class);
        System.out.println("myCustomizationBean1.getStudentAge:"+myCustomizationBean1.getStudentAge());
    }
}
