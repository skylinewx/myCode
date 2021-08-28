package com.example.spring.components;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

//@Component
public class MyBeanPostProcessor2 implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("cycleRefE") || beanName.equals("cycleRefF")) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(bean.getClass());
            enhancer.setCallback(new TestMethodInterceptor(bean));
            return enhancer.create();
        }
        return bean;
    }

    class TestMethodInterceptor implements MethodInterceptor {

        private final Object oriBean;

        TestMethodInterceptor(Object oriBean) {
            this.oriBean = oriBean;
        }

        @Override
        public Object intercept(Object object, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            return proxy.invokeSuper(object, args);
        }
    }
}
