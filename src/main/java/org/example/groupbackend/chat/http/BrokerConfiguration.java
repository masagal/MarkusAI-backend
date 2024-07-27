package org.example.groupbackend.chat.http;

import org.example.groupbackend.chat.ChatService;
import org.example.groupbackend.chat.ai.AiManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class BrokerConfiguration implements WebSocketConfigurer {
    AiManager aiManager;
    ChatService chatService;

    public BrokerConfiguration(AiManager aiManager, ChatService chatService) {
        this.aiManager = aiManager;
        this.chatService = chatService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler(), "/chat").setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler chatHandler() {
        return new ChatSocketHandler(aiManager, chatService);
    }
}
