package org.example.groupbackend.chat.ai;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface AiManager {
    String getNextResponse(List<JsonNode> conversationHistory) throws Exception;
}
