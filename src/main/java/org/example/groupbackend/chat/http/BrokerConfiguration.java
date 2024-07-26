package org.example.groupbackend.chat.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class BrokerConfiguration implements WebSocketConfigurer {
    private final JdbcTemplate jdbcTemplate;
    AiManager aiManager;

    public BrokerConfiguration(JdbcTemplate jdbcTemplate, AiManager aiManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.aiManager = aiManager;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler(), "/chat").setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler chatHandler() {
        return new ChatSocketHandler(jdbcTemplate, aiManager);
    }
}
