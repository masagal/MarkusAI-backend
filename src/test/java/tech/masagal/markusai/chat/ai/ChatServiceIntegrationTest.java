package tech.masagal.markusai.chat.ai;

import tech.masagal.markusai.chat.ChatMessage;
import tech.masagal.markusai.chat.PojoChatService;
import tech.masagal.markusai.inventory.model.InventoryItem;
import tech.masagal.markusai.inventory.service.InventoryService;
import tech.masagal.markusai.request.RequestListDto;
import tech.masagal.markusai.request.RequestProductDto;
import tech.masagal.markusai.request.RequestService;
import tech.masagal.markusai.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
public class ChatServiceIntegrationTest {
    @MockBean
    RestTemplate restTemplate;

    @Autowired
    PojoChatService chatService;
    @Autowired
    InventoryService inventoryService;

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

        var responseDtoEntity = new ResponseEntity<ChatGptResponseDto>(getDtoGivenMessage(jsonWithPojo), HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(ChatGptResponseDto.class))).thenReturn(responseDtoEntity);
    }

    @Test
    void shouldInsertAndRetrieveItems(@Autowired RequestService requestService) throws Exception {
        String requestJson = """
                {
                  "messageToUser": "I see that you're requesting more orange whiteboard markers. However, since their current stock is zero, I will prepare a request for you.",
                  "request": {
                    "products": [{"product_id": 1, "quantity": 4}]
                  }
                }
                """;
        ChatGptResponseDto dto = getDtoGivenMessage(requestJson);

        var responseDtoEntity = new ResponseEntity<>(dto, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(ChatGptResponseDto.class)))
                .thenReturn(responseDtoEntity);

        ChatMessage message = chatService.respondToUserMessage(null, new ChatMessage("Hey", ChatMessage.Role.USER));

        User mockAdmin = mock(User.class);
        when(mockAdmin.getIsAdmin()).thenReturn(true);
        var list = requestService.getAllRequests(mockAdmin);
        var lastRequest = list.get(list.size()-1);
        assertNotNull(lastRequest.getProducts());
        assertFalse(lastRequest.getProducts().isEmpty());
        assertNotNull(lastRequest.getProducts().get(0));
        assertEquals(4, lastRequest.getProducts().get(0).getQuantity());
        assertNotNull(lastRequest.getProducts().get(0).getProduct());
        Assertions.assertEquals(1, lastRequest.getProducts().get(0).getProduct().getId());
    }

    @Test
    void shouldUpdateInventoryOnRequest() throws Exception {
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

        ChatMessage message = chatService.respondToUserMessage(null, new ChatMessage("Hey", ChatMessage.Role.USER));

        InventoryItem item = inventoryService.getAll().stream()
                .filter((i) -> i.getProduct().getId().equals(1L))
                .findFirst().orElseThrow();

        assertEquals(0, item.getQuantity());
    }
}
