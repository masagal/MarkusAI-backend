package org.example.groupbackend.chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.groupbackend.chat.ai.AiManager;
import org.example.groupbackend.chat.http.ReadTemplate;
import org.example.groupbackend.chat.sql.ExtractSQL;
import org.example.groupbackend.chat.sql.SqlHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    private final Logger logger = LogManager.getLogger();
    private final AiManager aiManager;
    private final List<String> insertStatements = ReadTemplate.readTemplate();
    private final List<JsonNode> conversationHistory = new ArrayList<>();
    private final SqlHandler sqlHandler;

    public ChatService(AiManager aiManager, SqlHandler sqlHandler) {
        this.aiManager = aiManager;
        this.sqlHandler = sqlHandler;
    }

    public ChatMessage respondToUserMessage(ChatMessage chatMessage) throws Exception {
        String requestJson = getRequestJson(chatMessage, "Follow the instructions in the template");

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

            quantity = sqlHandler.queryForString(sql);
            logger.info("Quantity that was found with jdbc: " + quantity);

            if (quantity != null && Integer.parseInt(quantity) > 0) {
                logger.info("Response should be replaced here");
                response = "There are " + quantity + " of the item you are asking for. Do you want to make a request?";
            }
        }

        if (chatMessage.content().contains("yes") && response.contains("INSERT INTO")) {
            logger.info("We are in the if block to for making a new request");
            ExtractSQL sql = new ObjectMapper().readValue(response, ExtractSQL.class);
            logger.info("Statement: {}", sql.sqlStatement());
            sqlHandler.execute(sql);
            response = "Check your request page to see if the request has been made!";
        }

        String modifiedResponse = response.replace("OpenAI", "Markus.AI");

        logger.info("Sending response: {}", modifiedResponse);
        return new ChatMessage(modifiedResponse);
    }

    public void clearHistory() {
        conversationHistory.clear();
    }

    private String getRequestJson(ChatMessage message, String request) {
        return "{" +
                "message: " + message.content() + ", " +
                "template: " + insertStatements + "," +
                "request: " + request +
                "}";
    }
}
