package org.example.groupbackend.chat.manager;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface AiManager {
    String getNextResponse(List<JsonNode> conversationHistory) throws Exception;
}
