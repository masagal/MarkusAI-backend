package org.example.groupbackend.chat;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class SystemMessageTestConfiguration {

    @Bean(name = "ChatGPT Instructions")
    @Primary
    public ChatMessage systemMessage() {
        System.out.println("creating bena");
        return new ChatMessage("This is an automated test.", ChatMessage.Role.SYSTEM);
    }
}
