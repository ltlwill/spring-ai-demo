/**
 * @author TianLong Liu
 * @date 2026-04-21 14:25:56
 * @description
 */

package org.will.demo.web;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.will.demo.service.rabbitmq.RabbitMQProducer;

/**
 * @author TianLong Liu
 * @date 2026-04-21 14:25:56
 */
@RestController
@RequestMapping("/rabbitmq")
public class RabbitMqController {

    @Resource
    private RabbitMQProducer rabbitMQProducer;

    @RequestMapping("/send")
    public String sendMessage(String message) {
        rabbitMQProducer.sendMessage(message);
        return "发送成功";
    }

}
