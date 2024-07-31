package tech.masagal.markusai.chat.ai;

import tech.masagal.markusai.chat.ChatMessage;
import tech.masagal.markusai.user.User;

import java.util.List;

public interface AiManager {
    ChatMessage getNextResponse(List<ChatMessage> conversationHistory) throws Exception;
    ChatResult getChatCompletion(User user, List<ChatMessage> conversationHistory) throws Exception;
}
