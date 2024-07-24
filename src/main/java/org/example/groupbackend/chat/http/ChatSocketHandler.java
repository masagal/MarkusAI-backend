package org.example.groupbackend.chat.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Random;

public class ChatSocketHandler extends TextWebSocketHandler {
    Logger logger = LogManager.getLogger();

    private String[] possibleResponses = {"huh?", "yeah?", "nah", "tbh nope", "i need a drink"};

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.error("Wow! Check it: {}", message.getPayload());
        Random random = new Random();
        int index = random.nextInt(possibleResponses.length);
        var response = new TextMessage(possibleResponses[index]);
        session.sendMessage(response);
    }
}
