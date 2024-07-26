package org.example.groupbackend.chat.ai;

import org.example.groupbackend.chat.ChatMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SystemMessageConfiguration {
    private final List<String> insertStatements = ReadTemplate.readTemplate();

    @Bean(name = "ChatGPT Instructions")
    public ChatMessage systemMessage() {
        return new ChatMessage("" + insertStatements, ChatMessage.Role.SYSTEM);
    }
}
