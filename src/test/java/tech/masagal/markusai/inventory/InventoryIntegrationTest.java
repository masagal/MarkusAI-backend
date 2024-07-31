package tech.masagal.markusai.inventory;

import tech.masagal.markusai.inventory.http.InventoryController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
class InventoryIntegrationTest {

    MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void shouldGetInventoryItems() throws Exception {
        var get = MockMvcRequestBuilders.get(InventoryController.ENDPOINT);

        mockMvc.perform(get)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)));
    }

    @Test
    public void shouldUpdateItemQuantity() throws Exception {
        String updatedQuantityRequestBody = """
                {"id": "3", "quantity": "10"}
                """;

        var get = MockMvcRequestBuilders.get(InventoryController.ENDPOINT);
        var patch = MockMvcRequestBuilders.patch(InventoryController.ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedQuantityRequestBody);

        mockMvc.perform(patch)
                .andExpect(status().is(HttpStatus.ACCEPTED.value()));

        mockMvc.perform(get)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[2].quantity", is("10")));
    }

    @ParameterizedTest
    @DirtiesContext
    @ValueSource(strings = {"""
                {"label": "Grue whiteboard marker"}
                """, """
                {"label": "Purple whiteboard marker", "quantity": "4"}
                """})
    public void shouldCreateNewItemType(String body) throws Exception{

        var post = MockMvcRequestBuilders.post(InventoryController.ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);

        mockMvc.perform(post)
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andExpect(jsonPath("$.id", not(is(empty()))));

    }

    @Test
    public void shouldKnowLocation() throws Exception {
        var get = MockMvcRequestBuilders.get(InventoryController.ENDPOINT);

        mockMvc.perform(get)
                .andExpect(jsonPath("$.[0].location", not(is(empty()))));
    }

    @Test
    @DirtiesContext
    public void shouldDeleteItem() throws Exception {
        var delete = MockMvcRequestBuilders.delete(InventoryController.ENDPOINT + "/4");

        mockMvc.perform(delete)
                .andExpect(status().isNoContent());
    }
}