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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChatSocketHandler extends TextWebSocketHandler {
    Logger logger = LogManager.getLogger();

    private final List<JsonNode> conversationHistory = new ArrayList<>();

    private final JdbcTemplate jdbcTemplate;

    private final List<String> insertStatements = ReadTemplate.readTemplate();
    AiManager aiManager;


    public ChatSocketHandler(JdbcTemplate jdbcTemplate, AiManager aiManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.aiManager = aiManager;
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
            String response = aiManager.getNextResponse(conversationHistory);

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


}
