package org.example.groupbackend.chat.ai.dto;

import org.example.groupbackend.chat.ChatMessage;
import org.example.groupbackend.chat.ai.ChatGptResponseDto;

public class ChoicesMessage {
    String content;
    ChatMessage.Role role;

    public String content() {
        return content;
    }
}
