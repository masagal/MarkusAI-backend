package tech.masagal.markusai.chat.ai.dto;

import tech.masagal.markusai.chat.ChatMessage;

public class ChoicesMessage {
    String content;
    ChatMessage.Role role;

    public String content() {
        return content;
    }
}
