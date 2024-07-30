package org.example.groupbackend.chat.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.groupbackend.chat.ChatMessage;
import org.example.groupbackend.chat.PojoChatService;
import org.example.groupbackend.chat.ai.dto.ChoicesMessageDto;
import org.example.groupbackend.chat.ai.dto.ChoicesMessageWithPojoDto;
import org.example.groupbackend.request.RequestListDto;
import org.example.groupbackend.request.RequestProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class PojoChatGptManagerTest {
    @MockBean
    RestTemplate restTemplate;
    @MockBean
    PojoChatService service;
    @MockBean(name = "pojoChatGptInstructions")
    ChatMessage systemMessage;

    @Autowired
    PojoChatGptManager manager;

    ChatGptResponseDto basicResponseDto;

    ChatGptResponseDto getDtoGivenMessage(String message) {
        ChatGptResponseDto.ChoicesMessageDto messageWithPojoDto = new ChatGptResponseDto.ChoicesMessageDto("assistant", message);
        ChatGptResponseDto.ChoicesDto choicesDto = new ChatGptResponseDto.ChoicesDto(
                "0",
                messageWithPojoDto,
                null,
                "stop");

        return new ChatGptResponseDto("chatcmpl-9pXgtM6QSi9sPlrC6GA0g9hyg31b3",
                "chat.completion",
                "1722071263",
                "gpt-4o-mini-2024-07-18",
                List.of(choicesDto),
                new ChatGptResponseDto.UsageDataDto(19, 9, 28),
                "fp_ba606877f9");
    }

    @BeforeEach
    void setup() {
        when(systemMessage.content()).thenReturn("This is an automated test.");
        when(systemMessage.role()).thenReturn(ChatMessage.Role.SYSTEM);

        String messageToUser = "this is what it would seem like if there were a pojo message.";

        RequestListDto requestListDto = new RequestListDto(
                List.of(new RequestProductDto("1", 2), new RequestProductDto("2", 4)),
                1L);

        String jsonWithPojo = """
                {
                    "messageToUser": "this is what it would seem like if there were a pojo message.",
                    "request": {"requests": [{"1", 2}, {"2", 4}], 1}
                }
                """;

        ChatGptResponseDto.ChoicesMessageDto messageWithPojoDto = new ChatGptResponseDto.ChoicesMessageDto("assistant", jsonWithPojo);
        ChatGptResponseDto.ChoicesDto choicesDto = new ChatGptResponseDto.ChoicesDto(
                "0",
                messageWithPojoDto,
                null,
                "stop");

        basicResponseDto = new ChatGptResponseDto("chatcmpl-9pXgtM6QSi9sPlrC6GA0g9hyg31b3",
                "chat.completion",
                "1722071263",
                "gpt-4o-mini-2024-07-18",
                List.of(choicesDto),
                new ChatGptResponseDto.UsageDataDto(19, 9, 28),
                "fp_ba606877f9");

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
                                "content": "{
                                    "messageToUser": "this is what it would seem like if there were a pojo message.",
                                    "request": {"requests": [{"1", 2}, {"2", 4}], 1}
                                }"
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

        var responseDtoEntity = new ResponseEntity<ChatGptResponseDto>(basicResponseDto, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(ChatGptResponseDto.class))).thenReturn(responseDtoEntity);
    }

    @Test
    void shouldFormFullJson() throws Exception {
        String expectedOutput = """
                {
                    "model" : "gpt-4o-mini",
                    "messages" : [ {
                        "role" : "system",
                        "content" : "This is an automated test."
                    }, {
                        "role" : "user",
                        "content" : "Hello!"
                    } ],
                    "max_tokens" : 150
                }
                """;
        ArgumentCaptor<HttpEntity<String>> entityCaptor = ArgumentCaptor.captor();

        manager.getNextResponse(List.of(new ChatMessage("Hello!", ChatMessage.Role.USER)));

        verify(restTemplate).postForEntity(anyString(), entityCaptor.capture(), eq(ChatGptResponseDto.class));
        HttpEntity<String> entity = entityCaptor.getValue();

        ObjectMapper objectMapper = new ObjectMapper();
        Object expectedJsonObject = objectMapper.readValue(expectedOutput, Object.class);
        Object actualJsonObject = objectMapper.readValue(entity.getBody(), Object.class);

        assertEquals(expectedJsonObject, actualJsonObject);
    }

    @Test
    void shouldParseJsonCorrectly() {

    }

    @Test
    void shouldGetRequest() throws Exception {
        String nullRequestJson = """
                {
                  "messageToUser": "I see that you're requesting more orange whiteboard markers. However, since their current stock is zero, I will prepare a request for you.",
                  "request": {
                    "products": [{"product_id": 3, "quantity": 4}]
                  }
                }
                """;
        ChatGptResponseDto dto = getDtoGivenMessage(nullRequestJson);

        var responseDtoEntity = new ResponseEntity<>(dto, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(ChatGptResponseDto.class)))
                .thenReturn(responseDtoEntity);

        ChatResult result = manager.getChatCompletion(List.of(new ChatMessage("Hello!", ChatMessage.Role.USER)));

        assertTrue(result.request().isPresent());
        /*assertFalse(result.request().get().getProducts().isEmpty());
        assertEquals(4, result.request().get().getProducts().get(0).getQuantity());
        assertEquals(3, result.request().get().getProducts().get(0).getId());*/
    }

    @Test
    void shouldGetInventoryUpdateNullRequest() throws Exception {
        String invUpdateRequestJson = """
                {
                  "messageToUser": "I see that you think we are out of blue markers. I will update this accordingly.",
                  "inventoryUpdateRequest": {
                    "product_id": 1, "newQuantity": 0
                  }
                }
                """;
        ChatGptResponseDto dto = getDtoGivenMessage(invUpdateRequestJson);

        var responseDtoEntity = new ResponseEntity<>(dto, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(ChatGptResponseDto.class)))
                .thenReturn(responseDtoEntity);

        ChatResult result = manager.getChatCompletion(List.of(new ChatMessage("We are out of blue markers!", ChatMessage.Role.USER)));

        assertTrue(result.inventoryUpdateRequest().isPresent());
        assertEquals(0, result.inventoryUpdateRequest().get().newQuantity());
        assertEquals(1, result.inventoryUpdateRequest().get().productId());
    }


    @Test
    void shouldManageIfRequestIsntThere() throws Exception {
        String nullRequestJson = """
                {
                  "messageToUser": "I'm sorry, but regular milk is not available in our inventory. However, you may consider requesting Oatly oat milk, as we have 5 litres in stock.",
                  "request": null
                }
                """;
        ChatGptResponseDto dto = getDtoGivenMessage(nullRequestJson);

        var responseDtoEntity = new ResponseEntity<>(dto, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(ChatGptResponseDto.class)))
                .thenReturn(responseDtoEntity);

        manager.getChatCompletion(List.of(new ChatMessage("Hello!", ChatMessage.Role.USER)));

    }
}