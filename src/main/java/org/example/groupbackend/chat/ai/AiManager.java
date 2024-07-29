package org.example.groupbackend.chat.ai;

import org.example.groupbackend.chat.ChatMessage;

import java.util.List;

public interface AiManager {
    ChatMessage getNextResponse(List<ChatMessage> conversationHistory) throws Exception;
    ChatResult getChatCompletion(List<ChatMessage> conversationHistory) throws Exception;
}
