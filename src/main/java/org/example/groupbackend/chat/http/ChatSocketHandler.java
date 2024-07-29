package org.example.groupbackend.chat.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.groupbackend.chat.ChatMessage;
import org.example.groupbackend.chat.ChatService;
import org.example.groupbackend.chat.ai.AiManager;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ChatSocketHandler extends TextWebSocketHandler {
    Logger logger = LogManager.getLogger();
    ChatService chatService;

    public ChatSocketHandler(AiManager aiManager, ChatService chatService) {
        this.chatService = chatService;
    }

    private final JdbcTemplate jdbcTemplate;

    private final List<String> insertStatements = ReadTemplate.readTemplate();


    public ChatSocketHandler(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Called after a WebSocket connection is established
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        logger.info("WebSocket connection established: {}", session.getId());
    }

    // Handle incoming WebSocket text messages
    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) {
        logger.info("Received message: {}", message.getPayload());

        String holder = "Build one sql statement to update " +
                "the quantity of the item in the message based on the template and put it in json " +
                "format with key sqlStatement. If the item is not in the template only respond with " +
                "'This item is not in the inventory'";

        try {
            ChatMessage response = chatService.respondToUserMessage(new ChatMessage(message, ChatMessage.Role.USER));
            session.sendMessage(response.getTextMessage());
        } catch (Exception e) {
            // Log error and close session with server error status
            logger.error("Error handling message: {}", e.getMessage(), e);
            try {
                session.close(CloseStatus.SERVER_ERROR);
            } catch (Exception closeException) {
                logger.error("Error closing session: {}", closeException.getMessage(), closeException);
            }
        }
    }

    // Handle transport errors during WebSocket communication
    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) {
        logger.error("Transport error: {}", exception.getMessage(), exception);
    }

    // Called after a WebSocket connection is closed
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        logger.info("WebSocket connection closed: {} with status: {}", session.getId(), status);
        // Clear the conversation history when the session is closed
        chatService.clearHistory();
    }
}
