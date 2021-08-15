package com.example.spring.components;

import com.example.spring.customization.MyCustomizationBean1;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

@Component
public class MyBeanDefinitionRegisterPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        //从registry中获取名为student的bean的BeanDefinition
        BeanDefinition student = registry.getBeanDefinition("student");
        //获取student的propertyValues并添加age=20的属性
        student.getPropertyValues().add("age", 20);
        //创建一个通用的GenericBeanDefinition
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        //设置beanClass为MyCustomizationBean1
        genericBeanDefinition.setBeanClass(MyCustomizationBean1.class);
        //将创建的BeanDefinition注册到registry中
        registry.registerBeanDefinition("myCustomizationBean1", genericBeanDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
