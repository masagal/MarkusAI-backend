package tech.masagal.markusai.chat;

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

    public ChatMessage(String message) {
        this(message, Role.ASSISTANT);
    }
    public ChatMessage(TextMessage message, ChatMessage.Role role) {
        this(message.getPayload(), role);
    }

    public TextMessage getTextMessage() {
        return new TextMessage(this.content);
    }
}
