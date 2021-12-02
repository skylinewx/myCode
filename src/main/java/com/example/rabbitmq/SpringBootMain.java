package com.example.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangxing
 * @date 2021/12/2
 **/
@SpringBootApplication(scanBasePackages = "com.example.rabbitmq")
public class SpringBootMain {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringBootMain.class, args);
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        Channel channel = connection.createChannel(false);
        AMQP.Queue.DeclareOk declareOk = null;
        try {
            declareOk = channel.queueDeclarePassive("wx-max5");
        } catch (IOException e) {
            e.printStackTrace();
        }
        rabbitTemplate.convertAndSend("wx-max5", "1");
        int messageCount1 = declareOk.getMessageCount();
        rabbitTemplate.convertAndSend("wx-max5", "2");
        int messageCount2 = declareOk.getMessageCount();
        rabbitTemplate.convertAndSend("wx-max5", "3");
        int messageCount3 = declareOk.getMessageCount();
        rabbitTemplate.convertAndSend("wx-max5", "4");
        int messageCount4 = declareOk.getMessageCount();
        System.out.printf("2");
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }

    @Bean
    public Queue getMax5Queue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-length", 5);
        args.put("x-overflow", "reject-publish");
        return new Queue("wx-max5", true, false, true, args);
    }

    @Bean
    public Binding getMax5QueueBinding() {
        return BindingBuilder.bind(getMax5Queue()).to(topicExchange()).with("wx-max5");
    }
}
