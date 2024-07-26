package org.example.groupbackend.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.groupbackend.chat.ai.AiManager;
import org.example.groupbackend.chat.sql.ExtractSQL;
import org.example.groupbackend.chat.sql.SqlHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    private final Logger logger = LogManager.getLogger();
    private final AiManager aiManager;
    private final List<ChatMessage> conversationHistory = new ArrayList<>();
    private final SqlHandler sqlHandler;

    public ChatService(AiManager aiManager, SqlHandler sqlHandler) {
        this.aiManager = aiManager;
        this.sqlHandler = sqlHandler;
    }

    public ChatMessage handleQuantity(ChatMessage incomingResponse) throws JsonProcessingException {

        ExtractSQL sql = new ObjectMapper().readValue(incomingResponse.content(), ExtractSQL.class);
        logger.info("Getting quantity, Statement: {}", sql.sqlStatement());

        String quantity = sqlHandler.queryForString(sql);
        logger.info("Quantity that was found with jdbc: " + quantity);

        if (quantity != null && Integer.parseInt(quantity) > 0) {
            logger.info("Response should be replaced here");
            return new ChatMessage("There are " + quantity + " of the item you are asking for. Do you want to make a request?", ChatMessage.Role.ASSISTANT);
        }
        return incomingResponse;
    }

    public ChatMessage makeRequest(ChatMessage incomingResponse) throws JsonProcessingException {
        ExtractSQL sql = new ObjectMapper().readValue(incomingResponse.content(), ExtractSQL.class);
        logger.info("Making request, Statement: {}", sql.sqlStatement());
        sqlHandler.execute(sql);
        return new ChatMessage("Check your request page to see if the request has been made!", ChatMessage.Role.ASSISTANT);
    }

    public ChatMessage respondToUserMessage(ChatMessage userMessage) throws Exception {

        // Add the user's message to the conversation history
        logger.error("sending msg {}", userMessage.content());
        conversationHistory.add(userMessage);
        logger.error("so convo history is now length {}", conversationHistory.size());

        // Get response from ChatGPT API
        ChatMessage response = aiManager.getNextResponse(conversationHistory);

        logger.error("got msg {}", response.content());
        // Add the assistant's response to the conversation history
        conversationHistory.add(response);
        logger.error("so convo history is now length {}", conversationHistory.size());

        logger.info("Response from gpt: " + response);
        String quantity = "Problem finding quantity of the item";
        if (response.content().contains("sqlStatement") && response.content().contains("SELECT quantity FROM inventory_items")) {
            response = handleQuantity(response);
        }

        if (userMessage.content().contains("yes") && response.content().contains("INSERT INTO")) {
            response = makeRequest(response);
        }

        logger.info("Sending response: {}", response.content());
        return response;
    }

    public void clearHistory() {
        conversationHistory.clear();
    }
}
