package com.twiagle.dispike.spike.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    public static final String SPIKE_QUEUE = "spike.queue";

    @Bean
    public Queue queue() {
        return new Queue(SPIKE_QUEUE, true);
    }
}
