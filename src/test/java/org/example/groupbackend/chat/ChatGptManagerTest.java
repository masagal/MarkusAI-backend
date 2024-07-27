package org.example.groupbackend.chat;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.example.groupbackend.chat.ai.ChatGptManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
public class ChatGptManagerTest {
    @Mock(name = "ChatGPT Instructions")
    ChatMessage systemMessage;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    ChatGptManager manager;

    @BeforeEach
    void setup() {
        when(systemMessage.content()).thenReturn("This is an automated test.");
        when(systemMessage.role()).thenReturn(ChatMessage.Role.SYSTEM);

        String responseJson = """
                                {
                                "id": "chatcmpl-9pXgtM6QSi9sPlrC6GA0g9hyg31b3",
                                "object": "chat.completion",
                                "created": 1722071263,
                                "model": "gpt-4o-mini-2024-07-18",
                                "choices": [
                        {
                            "index": 0,
                                "message": {
                            "role": "assistant",
                                    "content": "Hello! How can I assist you today?"
                        },
                            "logprobs": null,
                                "finish_reason": "stop"
                        }
                  ],
                        "usage": {
                            "prompt_tokens": 19,
                                    "completion_tokens": 9,
                                    "total_tokens": 28
                        },
                        "system_fingerprint": "fp_ba606877f9"
                }
                """;
        var responseEntity = new ResponseEntity<String>(responseJson, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(responseEntity);
    }

    @Test
    void shouldLoadContext() {

    }

    @Test
    @DirtiesContext
    void shouldCallOpenAiPlatform() throws Exception {
        //Act
        manager.getNextResponse(List.of(new ChatMessage("Hello", ChatMessage.Role.USER)));

        //Assert
        verify(restTemplate).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }

    @Test
    @DirtiesContext
    void shouldFormCorrectJson() throws Exception {
        ArgumentCaptor<HttpEntity<String>> entityCaptor = ArgumentCaptor.captor();

        manager.getNextResponse(List.of(new ChatMessage("Hello", ChatMessage.Role.USER)));

        verify(restTemplate).postForEntity(anyString(), entityCaptor.capture(), eq(String.class));


        String pathHasModel = "$.model";
        String pathHasMessagesOfNonZeroLength = "$.messages[0]";
        String messageHasRole = "$.messages[0].role";
        String messageHasContent = "$.messages[0].content";
        HttpEntity<String> entity = entityCaptor.getValue();

        try {
            JsonPath.read(entity.getBody(), pathHasModel);
            JsonPath.read(entity.getBody(), pathHasMessagesOfNonZeroLength);
            JsonPath.read(entity.getBody(), messageHasContent);
            JsonPath.read(entity.getBody(), messageHasRole);
            // reading without throwing means the path is there, so the json is formed correctly
        } catch(PathNotFoundException ex) {
            fail("The json was incorrectly formed and was missing:" + ex.getMessage());
        }
    }
}
