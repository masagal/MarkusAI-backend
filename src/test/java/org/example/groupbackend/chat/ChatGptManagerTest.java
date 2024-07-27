package org.example.groupbackend.chat;

import org.example.groupbackend.chat.ai.ChatGptManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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

    }

    @Test
    void shouldLoadContext() {

    }

    @Test
    void shouldCallOpenAiPlatform() throws Exception {
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

        //Act
        manager.getNextResponse(List.of(new ChatMessage("Hello", ChatMessage.Role.USER)));

        //Assert
        verify(restTemplate).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }
}
