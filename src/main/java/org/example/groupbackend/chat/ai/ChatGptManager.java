package org.example.groupbackend.chat.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.groupbackend.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatGptManager implements AiManager {
    Logger logger = LogManager.getLogger();

    ChatMessage systemMessage;

    @Value("${OPENAI_API_KEY}")
    private String apiKey;
    private final HttpHeaders headers = new HttpHeaders();
    private final RestTemplate restTemplate;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    @Autowired
    public ChatGptManager(@Qualifier("ChatGPT Instructions") ChatMessage systemMessage, RestTemplate restTemplate) {
        this.systemMessage = systemMessage;
        this.restTemplate = restTemplate;

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
    }

    private String writeConversationHistory(List<ChatMessage> conversationHistory) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(conversationHistory);
    }

    public ChatMessage getNextResponse(List<ChatMessage> conversationHistory) throws Exception {
        ArrayList<ChatGptMessageDto> messages = new ArrayList<>(List.of(new ChatGptMessageDto(systemMessage)));
        messages.addAll(conversationHistory.stream().map(ChatGptMessageDto::new).toList());
        ChatGptInputDto dto = new ChatGptInputDto("gpt-4o-mini", messages, 150);

        HttpEntity<String> entity = new HttpEntity<>(new ObjectMapper().writeValueAsString(dto), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            return new ChatMessage(root.path("choices").get(0).path("message").path("content").asText(), ChatMessage.Role.ASSISTANT);
        } else {
            logger.error("Error response from OpenAI: {} - {}", response.getStatusCode(), response.getBody());
            throw new Exception("Error response from OpenAI API");
        }
    }
}
