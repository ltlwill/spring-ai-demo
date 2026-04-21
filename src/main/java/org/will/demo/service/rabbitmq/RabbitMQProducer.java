package org.will.demo.service.rabbitmq;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.will.demo.config.RabbitMQConfig;
import org.will.demo.constants.RabbitMqConstants;

/**
 * 消息生产者：发送消息到 RabbitMQ
 */
@Service
public class RabbitMQProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     * @param message 消息内容
     */
    public void sendMessage(String message) {
        System.out.println("发送消息：" + message);
        rabbitTemplate.convertAndSend(RabbitMqConstants.TEST_EXCHANGE, RabbitMqConstants.TEST_ROUTING_KEY, message);
    }
}
