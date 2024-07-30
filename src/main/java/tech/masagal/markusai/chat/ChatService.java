package tech.masagal.markusai.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.masagal.markusai.chat.ai.AiManager;
import tech.masagal.markusai.chat.sql.ExtractSQL;
import tech.masagal.markusai.chat.sql.SqlHandler;
import org.springframework.stereotype.Service;
import tech.masagal.markusai.user.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    private final Logger logger = LogManager.getLogger();
    final AiManager aiManager;
    protected final List<ChatMessage> conversationHistory = new ArrayList<>();
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
        conversationHistory.add(userMessage);
        ChatMessage response = aiManager.getNextResponse(conversationHistory);
        conversationHistory.add(response);

        logger.info("Response from gpt: " + response);
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
