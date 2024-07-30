package tech.masagal.markusai.chat.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import tech.masagal.markusai.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Component
@Primary
public class PojoChatGptManager extends ChatGptManager implements AiManager {


    public PojoChatGptManager(@Qualifier("pojoChatGptInstructions") ChatMessage systemMessage,
                              RestTemplate restTemplate,
                              @Value("${openai.api.key}") String apiKey) {

        super(systemMessage, restTemplate, apiKey);
    }

    private ChatResult parseChoice(ChatGptResponseDto.ChoicesDto choice) {
        ChatGptResponseDto.ChoicesMessageDto message = choice.message();
        String json = message.content();
        logger.info("Trying to parse ChatGpt response: {}", json);

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new Jdk8Module());
        try {
            json = json.substring(json.indexOf('{'), json.lastIndexOf('}')+1);
            return om.readValue(json, ChatResult.class);
        } catch(Exception ex) {
            logger.error("Trying to parse a choice from Chat, but running into a JSON error. {}",
                    ex.getMessage());
            return new ChatResult(json, Optional.empty(), Optional.empty());
        }
    }

    @Override
    public ChatResult getChatCompletion(List<ChatMessage> conversationHistory) throws Exception {
        ResponseEntity<ChatGptResponseDto> response = getResponse(conversationHistory);

        if(response.getStatusCode().is2xxSuccessful()) {
            try {
                ChatGptResponseDto.ChoicesDto choice = response.getBody().choices().get(0);
                return parseChoice(choice);
            } catch(NullPointerException ex) {
                throw new IllegalStateException("ChatGPT responded 2xx, but the DTO lacked choices.");
            }
        } else {
            String message = String.join(" ", "Request to ChatGpt failed. ",
                    response.getStatusCode().toString(),
                    response.getHeaders().toString(),
                    response.getBody().toString());
            throw new IllegalStateException(message);
        }
    }

}
