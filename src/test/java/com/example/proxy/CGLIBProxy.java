package com.example.proxy;

import org.junit.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLIB动态代理学习
 */
public class CGLIBProxy {

    @Test
    public void test1() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Student.class);
        enhancer.setCallback(new TestMethodInterceptor());
        Student proxyStudent = (Student) enhancer.create();
        proxyStudent.setAge(23);
        proxyStudent.setName("wx");
        System.out.println(proxyStudent);
    }

    static class TestMethodInterceptor implements MethodInterceptor {

        @Override
        public Object intercept(Object object, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            return proxy.invokeSuper(object, args);
        }
    }

    static class Student {
        private int age;
        private String name;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    '}' + "\r\n" + super.toString();
        }
    }
}
