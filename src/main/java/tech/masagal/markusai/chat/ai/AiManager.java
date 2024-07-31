package tech.masagal.markusai.chat.ai;

import tech.masagal.markusai.chat.ChatMessage;

import java.util.List;

public interface AiManager {
    ChatMessage getNextResponse(List<ChatMessage> conversationHistory) throws Exception;
    ChatResult getChatCompletion(List<ChatMessage> conversationHistory) throws Exception;
}
