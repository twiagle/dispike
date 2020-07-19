package com.twiagle.dispike.spike.rabbitmq;

import com.twiagle.dispike.spike.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {
    @Autowired
    AmqpTemplate amqpTemplate ;

    public void sendSpikeMessage(Object message) {
		String msg = RedisService.beanToString(message);
		log.info("send message:"+msg);
		amqpTemplate.convertAndSend(MQConfig.SPIKE_QUEUE, msg);
	}
}
