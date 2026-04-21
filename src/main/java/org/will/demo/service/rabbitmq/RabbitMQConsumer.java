package org.will.demo.service.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.will.demo.constants.RabbitMqConstants;

/**
 * 消息消费者：监听队列，自动接收消息
 */
@Service
public class RabbitMQConsumer {

    /**
     * 监听队列，自动接收消息
     */
    @RabbitListener(queues = RabbitMqConstants.TEST_QUEUE)
    public void receiveMessage(String message) {
        System.out.println("收到消息：" + message);
    }
}
