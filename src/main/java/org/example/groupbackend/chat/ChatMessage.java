package org.example.groupbackend.chat;

import org.springframework.web.socket.TextMessage;

public record ChatMessage(String content, Role role) {
    public enum Role {
        SYSTEM("system"),
        ASSISTANT("assistant"),
        USER("user");

        public final String name;
        Role(String name) {
            this.name = name;
        }
    }

    public ChatMessage(TextMessage message, ChatMessage.Role role) {
        this(message.getPayload(), role);
    }

    public TextMessage getTextMessage() {
        return new TextMessage(this.content);
    }
}
