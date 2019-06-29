package com.shu.miaosha.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author yang
 * @date 2019/6/29 21:16
 */
@Service
@Slf4j
public class MQReceiver {
    private final AmqpTemplate amqpTemplate;

    public MQReceiver(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message) {
        log.info(message);
    }
}
