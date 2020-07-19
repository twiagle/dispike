package com.twiagle.dispike.customer.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
@Data
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {

    private String host;
    private int port;

    private int timeout;
    private int poolMaxTotal;
    private int  poolMaxIdle;
    private int poolMaxWait;

}
