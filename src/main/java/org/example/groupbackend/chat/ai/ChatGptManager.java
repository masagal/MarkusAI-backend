package org.example.groupbackend.chat.ai;

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
import java.util.NoSuchElementException;

@Service
public class ChatGptManager implements AiManager {
    Logger logger = LogManager.getLogger();

    ChatMessage systemMessage;

    private String apiKey;
    private final HttpHeaders headers = new HttpHeaders();
    private final RestTemplate restTemplate;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    @Autowired
    public ChatGptManager(@Qualifier("ChatGPT Instructions") ChatMessage systemMessage, RestTemplate restTemplate, @Value("${openai.api.key}") String apiKey) {
        if(apiKey == null) {
            logger.error("OpenAI API key not found.");
            logger.error("It should be set in application.yml.");
            throw new IllegalStateException("OpenAI API key not found.");
        }
        this.apiKey = apiKey;
        this.systemMessage = systemMessage;
        this.restTemplate = restTemplate;

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
    }

    public ChatMessage getNextResponse(List<ChatMessage> conversationHistory) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        ArrayList<ChatGptMessageDto> messages = new ArrayList<>(List.of(new ChatGptMessageDto(systemMessage)));
        messages.addAll(conversationHistory.stream().map(ChatGptMessageDto::new).toList());
        ChatGptInputDto dto = new ChatGptInputDto("gpt-4o-mini", messages, 150);

        HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(dto), headers);

        ResponseEntity<ChatGptResponseDto> response = restTemplate.postForEntity(API_URL, entity, ChatGptResponseDto.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            List<ChatGptResponseDto.ChoicesDto> choices = response.getBody().choices();
            if(choices.isEmpty()) {
                throw new NoSuchElementException("ChatGPT returned zero responses.");
            }
            ChatGptResponseDto.ChoicesMessageDto message = choices.get(0).message();
            return new ChatMessage(message.content(), ChatMessage.Role.ASSISTANT);
        } else {
            logger.error("Error response from OpenAI: {} - {}", response.getStatusCode(), response.getBody());
            throw new Exception("Error response from OpenAI API");
        }
    }
}