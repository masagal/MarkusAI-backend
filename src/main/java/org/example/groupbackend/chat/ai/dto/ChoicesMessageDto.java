package org.example.groupbackend.chat.ai.dto;

import org.example.groupbackend.chat.ChatMessage;
import org.example.groupbackend.chat.ai.ChatGptResponseDto;

public class ChoicesMessageDto extends ChoicesMessage {
    public ChoicesMessageDto(String role, String s) {
        super();

        this.role = ChatMessage.Role.valueOf(role);
        this.content = s;
    }
}
