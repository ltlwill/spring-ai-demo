package org.will.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.will.demo.constants.RabbitMqConstants;

/**
 * RabbitMQ 配置类：定义队列、交换机、绑定
 */
@Configuration
public class RabbitMQConfig {



    // 1. 创建队列
    @Bean
    public Queue testQueue() {
        // true = 持久化队列
        return new Queue(RabbitMqConstants.TEST_QUEUE, true);
    }

    // 2. 创建 Topic 交换机（最常用）
//    @Bean
//    public TopicExchange testExchange() {
//        return new TopicExchange(RabbitMqConstants.TEST_EXCHANGE);
//    }

    @Bean
    public TopicExchange testExchange() {
        return new TopicExchange(RabbitMqConstants.TEST_EXCHANGE, true, false); // 交换机持久化。默认：持久化
    }



    // 3. 绑定队列到交换机
    @Bean
    public Binding testBinding() {
        return BindingBuilder.bind(testQueue())
                .to(testExchange())
                .with(RabbitMqConstants.TEST_ROUTING_KEY);
    }
}
