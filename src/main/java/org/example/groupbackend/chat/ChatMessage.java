package org.example.groupbackend.chat;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.w3c.dom.Text;

public record ChatMessage(String content) {
    public ChatMessage(TextMessage message) {
        this(message.getPayload());
    }

    public TextMessage getTextMessage() {
        return new TextMessage(this.content);
    }
}
