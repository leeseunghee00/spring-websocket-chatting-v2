package com.example.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
@RequiredArgsConstructor
public class RedisPubApplication implements CommandLineRunner {

    private final RedisTemplate<String, String> stringValueRedisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(RedisPubApplication.class);
    }

    @Override
    public void run(final String... args) {

        // TEST: String 메시지 전송
        stringValueRedisTemplate.convertAndSend("ch01", "Apple, Orange");
    }
}