package org.example.groupbackend.chat.ai;

import java.util.List;

public record ChatGptInputDto(String model, List<ChatGptMessageDto> messages, Integer max_tokens) {
}
