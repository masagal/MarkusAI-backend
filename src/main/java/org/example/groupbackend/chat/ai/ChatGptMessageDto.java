package org.example.groupbackend.chat.ai;

import org.example.groupbackend.chat.ChatMessage;

public record ChatGptMessageDto(String role, String content) {
    public ChatGptMessageDto(ChatMessage inputMessage) {
        this(inputMessage.role().name, inputMessage.content());
    }
}
