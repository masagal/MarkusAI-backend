package tech.masagal.markusai.chat.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.WebSocketSession;
import tech.masagal.markusai.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tech.masagal.markusai.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ChatGptManager implements AiManager {
    Logger logger = LogManager.getLogger();

    @Autowired
    ApplicationContext context;

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

    ResponseEntity<ChatGptResponseDto> getResponse(List<ChatMessage> conversationHistory) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        ChatMessage systemMessage = context.getBean(ChatMessage.class);
        User user = context.getBean("user", User.class);
        ChatMessage systemMessage2 = new ChatMessage(systemMessage.content() + "\nYou are talking to: " + user.getName(), ChatMessage.Role.SYSTEM);
        logger.info("telling chat they are talking to " + user.getName());

        ArrayList<ChatGptMessageDto> messages = new ArrayList<>(List.of(new ChatGptMessageDto(systemMessage2)));
        messages.addAll(conversationHistory.stream().map(ChatGptMessageDto::new).toList());
        ChatGptInputDto dto = new ChatGptInputDto("gpt-4o-mini", messages, 150);
        HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(dto), headers);

        return restTemplate.postForEntity(API_URL, entity, ChatGptResponseDto.class);
    }

    public ChatMessage getNextResponse(List<ChatMessage> conversationHistory) throws Exception {
        ResponseEntity<ChatGptResponseDto> response = getResponse(conversationHistory);

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

    @Override
    public ChatResult getChatCompletion(List<ChatMessage> conversationHistory) throws Exception {
        ResponseEntity<ChatGptResponseDto> response = getResponse(conversationHistory);

        if (response.getStatusCode().is2xxSuccessful()) {
            List<ChatGptResponseDto.ChoicesDto> choices = response.getBody().choices();
            if(choices.isEmpty()) {
                throw new NoSuchElementException("ChatGPT returned zero responses.");
            }
            ChatGptResponseDto.ChoicesMessageDto message = choices.get(0).message();
            return new ChatResult(message.content(), null, null);
        } else {
            logger.error("Error response from OpenAI: {} - {}", response.getStatusCode(), response.getBody());
            throw new Exception("Error response from OpenAI API");
        }
    }
}
