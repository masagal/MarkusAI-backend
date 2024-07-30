package tech.masagal.markusai.chat.ai.dto;

import tech.masagal.markusai.chat.ChatMessage;

public class ChoicesMessageDto extends ChoicesMessage {
    public ChoicesMessageDto(String role, String s) {
        super();

        this.role = ChatMessage.Role.valueOf(role);
        this.content = s;
    }
}
