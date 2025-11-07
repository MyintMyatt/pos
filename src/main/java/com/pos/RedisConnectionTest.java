package com.pos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisConnectionTest implements CommandLineRunner {

    private final StringRedisTemplate redisTemplate;

    public RedisConnectionTest(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        String pong = redisTemplate.getConnectionFactory()
                .getConnection()
                .ping();
        System.err.println("✅ Redis Connection Test: " + pong);
    }
}

