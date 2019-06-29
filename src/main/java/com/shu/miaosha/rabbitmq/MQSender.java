package com.shu.miaosha.rabbitmq;

import com.shu.miaosha.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

/**
 * @author yang
 * @date 2019/6/29 21:16
 */
@Service
@Slf4j
public class MQSender {
    private final AmqpTemplate amqpTemplate;

    public MQSender(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void send(Object message) {
        String msg = RedisService.beanToString(message);
        amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);
    }
    public void sendMiaoshaMessage(MiaoshaMessage mmessage) {
        // 将对象转换为字符串
        String msg = RedisService.beanToString(mmessage);
        log.info("send message:" + msg);
        // 第一个参数队列的名字，第二个参数发出的信息
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
    }
}
