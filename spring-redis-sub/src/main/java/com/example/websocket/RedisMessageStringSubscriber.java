package com.example.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

/**
 * 메시지 수신 처리 로직
 */

@Slf4j
@Service
public class RedisMessageStringSubscriber implements MessageListener {

    public void onMessage(final Message message, final byte[] pattern) {
        log.info("String Message received: " + message.toString());
    }
}
