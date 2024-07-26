package org.example.groupbackend.chat;

import jakarta.persistence.ManyToOne;
import org.example.groupbackend.chat.ai.AiManager;
import org.example.groupbackend.chat.sql.ExtractSQL;
import org.example.groupbackend.chat.sql.SqlHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

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

    @Test
    void shouldLoad() {

    }

    @BeforeEach
    void setup() {

    }

    @Test
    void shouldExtractSqlAndExecute() throws Exception {
        // Arrange
        String query = "SELECT quantity FROM inventory_items WHERE product_id=(SELECT id FROM products WHERE name='Whiteboard markers')";
        ExtractSQL sql = new ExtractSQL(query);
        String response = "{\n" +
                "  \"sqlStatement\": \""+query+"\" \n" +
                "}";
        when(mockAiManager.getNextResponse(any())).thenReturn(response);

        //Act
        service.respondToUserMessage(new ChatMessage("Hello! Could I get five whiteboard markers please?"));

        //Assert
        verify(sqlHandler).queryForString(sql);
    }
}
