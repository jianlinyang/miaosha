package com.shu.miaosha.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yang
 * @date 2019/6/29 21:17
 */
@Configuration
public class MQConfig {
    public static final String QUEUE = "queue";
    public static final String MIAOSHA_QUEUE="miaosha.queue";

    @Bean
    public Queue queue(){
        return new Queue(QUEUE,true);
    }
    @Bean
    public Queue miaoshaqueue() {
        //名称，是否持久化
        return new Queue(MIAOSHA_QUEUE,true);
    }
}
