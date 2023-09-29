package com.example.websocket.handler;

import com.example.websocket.Message;
import com.example.websocket.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* STOMP 사용 시 핸들러 주석 처리 후 실행 */

@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    /**
     * 웹소켓 연결
     */
    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        final var sessionId = session.getId();

        sessions.put(sessionId, session);   // 1) 세션 저장

        final Message message = Message.builder()
                .sender(sessionId)
                .receiver("all")
                .build();
        message.newConnect();

        sessions.values().forEach(s->{  // 2) 모든 세션에 알림
            try{
                if (!s.getId().equals(sessionId))
                    s.sendMessage(new TextMessage(Utils.getString(message)));
            } catch (final Exception e) {
            }
        });
    }

    /**
     * 양방향 데이터 통신
     */
    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage textMessage) throws Exception {
        final Message message = Utils.getObject(textMessage.getPayload());

        message.setSender(session.getId()); // 1) 메시지 송신자의 세션 ID 를 담는다.

        final WebSocketSession receiver = sessions.get(message.getReceiver());  // 2) 메시지를 수신할 상대방을 찾는다.

        // 3) 타겟이 존재하고 연결된 상태라면, 메시지를 전송한다.
        if (receiver != null && receiver.isOpen())
            receiver.sendMessage(new TextMessage(Utils.getString(message)));
    }

    /**
     * 소켓 연결 종료
     */
    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
        final var sessionId = session.getId();

        sessions.remove(sessionId);     // 1) 세션 저장소에서 연결이 끊긴 사용자를 삭제한다.

        final Message message = new Message();
        message.closeConnect();
        message.setSender(sessionId);

        sessions.values().forEach(s -> {
            try {
                // 2) 다른 사용자에게 끊긴 사용자를 알려준다.
                s.sendMessage(new TextMessage(Utils.getString(message)));
            } catch (final Exception e) {
            }
        });
    }

    /**
     * 소켓 통신 에러
     */
    @Override
    public void handleTransportError(final WebSocketSession session, final Throwable exception) throws Exception {
    }
}
