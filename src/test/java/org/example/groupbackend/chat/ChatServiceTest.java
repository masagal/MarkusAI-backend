package org.example.groupbackend.chat;

import org.example.groupbackend.chat.ai.AiManager;
import org.example.groupbackend.chat.ai.ReadTemplate;
import org.example.groupbackend.chat.sql.ExtractSQL;
import org.example.groupbackend.chat.sql.SqlHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ChatServiceTest {

    @MockBean
    AiManager mockAiManager;
    @MockBean
    SqlHandler sqlHandler;

    @Autowired
    @InjectMocks
    ChatService service;

    private final List<String> insertStatements = ReadTemplate.readTemplate();

    @Test
    void shouldLoad() {

    }

    @BeforeEach
    void setup() {

    }

    @Test
    @DirtiesContext
    void shouldExtractSqlAndExecute() throws Exception {
        // Arrange
        String query = "SELECT quantity FROM inventory_items WHERE product_id=(SELECT id FROM products WHERE name='Whiteboard markers')";
        ExtractSQL sql = new ExtractSQL(query);
        String response = "{\n" +
                "  \"sqlStatement\": \""+query+"\" \n" +
                "}";
        when(mockAiManager.getNextResponse(any())).thenReturn(new ChatMessage(response, ChatMessage.Role.ASSISTANT));

        //Act
        service.respondToUserMessage(new ChatMessage("Hello! Could I get five whiteboard markers please?", ChatMessage.Role.USER));

        //Assert
        verify(sqlHandler).queryForString(sql);
    }

    @Test
    @DirtiesContext
    void shouldAppendToConversationHistory() throws Exception {
        //Arrange
        ChatMessage request1 = new ChatMessage("first request", ChatMessage.Role.USER);
        ChatMessage response1 = new ChatMessage("first response", ChatMessage.Role.ASSISTANT);
        when(mockAiManager.getNextResponse(any())).thenReturn(response1);

        //Act
        service.respondToUserMessage(request1);

        //Assert
        /*
            N.B. Funny thing about verifying this argument.
                The ArrayList with which getNextResponse is called is mutated after the call.
                We are not verifying the state of the ArrayList as it was when it was used to
                call getNextResponse, but verifying the state after the previous method ends.
                getNextResponse was actually called with just request1, but the ArrayList was
                mutated afterward, before we could verify it.   Took me a hot minute to grok.
         */
        verify(mockAiManager).getNextResponse(List.of(request1, response1));
    }

    @Test
    void test() {

    }
}
