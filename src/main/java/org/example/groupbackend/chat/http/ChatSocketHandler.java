package org.example.groupbackend.chat.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
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

@Component
public class ChatSocketHandler extends TextWebSocketHandler {
    Logger logger = LogManager.getLogger();

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private final List<JsonNode> conversationHistory = new ArrayList<>();

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

            String requestJson = getRequestJson(message,"Follow the instructions in the template" );

            // Add the user's message to the conversation history
            conversationHistory.add(new ObjectMapper().createObjectNode().put("role", "user").put("content", requestJson));

            // Get response from ChatGPT API
            String response = getChatGPTResponse();

            // Add the assistant's response to the conversation history
            conversationHistory.add(new ObjectMapper().createObjectNode().put("role", "assistant").put("content", response));

            logger.info("Response from gpt: " + response);
            String quantity = "Problem finding quantity of the item";
            if (response.contains("sqlStatement") && response.contains("SELECT quantity FROM inventory_items")) {
                logger.info("We are in the if block for getting quantity");
                ExtractSQL sql = new ObjectMapper().readValue(response, ExtractSQL.class);
                logger.info("Statement: {}", sql.sqlStatement());
                //jdbcTemplate.execute(sql.sqlStatement());
                quantity = this.jdbcTemplate.queryForObject(
                        sql.sqlStatement(),
                        String.class);
                logger.info("Quantity that was found with jdbc: " + quantity);
                if (quantity != null && Integer.parseInt(quantity) > 0) {
                    logger.info("Response should be replaced here" );

                    response = "There are " + quantity + " of the item you are asking for. Do you want to make a request?";
                }
            }

            if (message.getPayload().contains("yes") && response.contains("INSERT INTO")) {
                logger.info("We are in the if block to for making a new request");
                ExtractSQL sql = new ObjectMapper().readValue(response, ExtractSQL.class);
                logger.info("Statement: {}", sql.sqlStatement());
                jdbcTemplate.execute(sql.sqlStatement());
                response = "Check your request page to see if the request has been made!";
            }

            String modifiedResponse = response.replace("OpenAI", "Markus.AI");
            logger.info("Sending response: {}", modifiedResponse);
            // Send the response back to the WebSocket client

            session.sendMessage(new TextMessage(modifiedResponse));
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


    private String getRequestJson(TextMessage message, String request) {
        String requestJson = "{" +
                "message: " + message.getPayload() + ", " +
                "template: " + insertStatements + "," +
                "request: "+ request +
                "}";

        return requestJson;
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
