package tech.masagal.markusai.chat.ai;

import tech.masagal.markusai.chat.ChatMessage;

public record ChatGptMessageDto(String role, String content) {
    public ChatGptMessageDto(ChatMessage inputMessage) {
        this(inputMessage.role().name, inputMessage.content());
    }
}
