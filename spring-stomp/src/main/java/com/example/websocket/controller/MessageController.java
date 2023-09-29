package com.example.websocket.controller;

import com.example.websocket.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    /**
     *  클라이언트가 /pub/hello 로 메시지를 발행한다.
     *
     *      /sub/channel/channelID     - 구독
     *      /pub/hello                 - 메시지 발생
     */
    @MessageMapping("/hello")
    public void message(final Message message) {

        // - 해당 채널ID 에 메시지를 보낸다.
        // - 그리고 "/sub/channel/channelID" 에 구독 중인 클라이언트에게 메시지를 보낸다.
        simpMessageSendingOperations.convertAndSend("/sub/channel/" + message.getChannelId(), message);
    }
}
