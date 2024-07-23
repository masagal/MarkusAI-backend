package org.example.groupbackend.chat.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ChatSocketHandler extends TextWebSocketHandler {
    Logger logger = LogManager.getLogger();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        logger.error("Wow! Check it: {}", message.getPayload());
    }
}
