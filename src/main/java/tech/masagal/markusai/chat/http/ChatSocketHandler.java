package tech.masagal.markusai.chat.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.DynamicRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import tech.masagal.markusai.chat.ChatMessage;
import tech.masagal.markusai.chat.ChatService;
import tech.masagal.markusai.chat.ai.AiManager;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tech.masagal.markusai.filterRequests.SubDto;
import tech.masagal.markusai.user.User;
import tech.masagal.markusai.user.UserRepository;

import java.util.Base64;

@Component
public class ChatSocketHandler extends TextWebSocketHandler {
    Logger logger = LogManager.getLogger();
    ChatService chatService;
    UserRepository userRepo;

    @Autowired
    ConfigurableApplicationContext context;

    public ChatSocketHandler(AiManager aiManager, ChatService chatService, UserRepository userRepo) {
        this.chatService = chatService;
        this.userRepo = userRepo;
    }

    public ChatSocketHandler() {

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


        if(message.getPayload().contains("Set-Authorization")) {
            String token = message.getPayload().split(" ")[1];
            String[] chunks = token.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();

            String payload = new String(decoder.decode(chunks[1]));
            try {
                SubDto sub = new ObjectMapper().readValue(payload, SubDto.class);
                User user = userRepo.findByClerkId(sub.sub());
                session.getAttributes().put("user", user);
                return;
            } catch (Exception ex) {
                logger.error("Failed to map user data. This is a problem.");
                throw new IllegalStateException("failed to map user data" + ex.getMessage());
            }
        }
        User user = (User) session.getAttributes().get("user");
        if(user == null) {
            logger.error("User is null. That's a disaster.");
            throw new IllegalStateException("user may not be null.");
        }

        try {
            ChatMessage response = chatService.respondToUserMessage(user, new ChatMessage(message, ChatMessage.Role.USER));
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
        chatService.clearHistory();
    }
}
