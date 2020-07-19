package com.twiagle.dispike.spike.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
