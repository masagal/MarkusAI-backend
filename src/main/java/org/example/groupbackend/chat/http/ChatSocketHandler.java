package org.example.groupbackend.chat.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import java.util.ArrayList;
import java.util.List;

public class ChatSocketHandler extends TextWebSocketHandler {
    Logger logger = LogManager.getLogger();

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private final List<JsonNode> conversationHistory = new ArrayList<>();

    // Called after a WebSocket connection is established
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        logger.info("WebSocket connection established: {}", session.getId());
    }

    // Handle incoming WebSocket text messages
    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) {
        logger.info("Received message: {}", message.getPayload());

        try {
            // Add the user's message to the conversation history
            conversationHistory.add(new ObjectMapper().createObjectNode().put("role", "user").put("content", message.getPayload()));

            // Get response from ChatGPT API
            String response = getChatGPTResponse();

            // Add the assistant's response to the conversation history
            conversationHistory.add(new ObjectMapper().createObjectNode().put("role", "assistant").put("content", response));

            logger.info("Sending response: {}", response);
            // Send the response back to the WebSocket client
            session.sendMessage(new TextMessage(response));
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
        conversationHistory.clear();
    }

    // Method to get response from OpenAI's ChatGPT
    private String getChatGPTResponse() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        // Set content type to JSON and add Bearer token for authorization
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // Create request JSON with the conversation history
        String requestJson = "{" +
                "\"model\":\"gpt-4\"," +
                "\"messages\":" + new ObjectMapper().writeValueAsString(conversationHistory) + "," +
                "\"max_tokens\":150" +
                "}";
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        // Send POST request to the API
        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();
        } else {
            logger.error("Error response from OpenAI: {} - {}", response.getStatusCode(), response.getBody());
            throw new Exception("Error response from OpenAI API");
        }
    }
}
