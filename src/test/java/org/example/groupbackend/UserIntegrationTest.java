package org.example.groupbackend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.example.groupbackend.user.ClerkService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class UserIntegrationTest {

    ArrayList<String> createdUserIds = new ArrayList<>();

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void cleanup() {
        var service = (ClerkService) context.getBean(ClerkService.class);
        createdUserIds.forEach(service::deleteUser);
    }

    //fails because of secret not being exposed
    @Test
    public void shouldCreateUser() throws Exception {
        String uniqueId = String.valueOf(System.currentTimeMillis()); // Use current time as a unique ID
        String newUserRequestBody = String.format("""
            {
                "name": "Test User %s",
                "email": "testuser%s@example.com",
                "isAdmin": false
            }
            """, uniqueId, uniqueId);

        var post = MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserRequestBody);

        var result = mockMvc.perform(post)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Test User " + uniqueId)))
                .andExpect(jsonPath("$.email", is("testuser" + uniqueId + "@example.com")))
                .andExpect(jsonPath("$.isAdmin", is(false)))
                .andReturn();

        String createdUserId = JsonPath.read(result.getResponse().getContentAsString(), "$.clerkId");
        createdUserIds.add(createdUserId);
    }

    @Test
    public void shouldGetAllUsers() throws Exception {
        var get = MockMvcRequestBuilders.get("/api/users");

        mockMvc.perform(get)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(0)));
    }
// make data sql cause it cant retrieve something that dosen't exist
  /*  @Test
    public void shouldCreateAndGetUserById() throws Exception {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String newUserRequestBody = String.format("""
            {
                "name": "Test User %s",
                "email": "testuser%s@example.com",
                "isAdmin": false
            }
            """, uniqueId, uniqueId);

        var post = MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserRequestBody);

        var result = mockMvc.perform(post)
                .andExpect(status().isCreated())
                .andReturn();
        System.out.println("result = " + result);

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseBody);
        Long userId = root.path("id").asLong();
        System.out.println("Created User ID: " + userId);

        var get = MockMvcRequestBuilders.get("/api/users/" + userId);

        mockMvc.perform(get)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.intValue())))
                .andExpect(jsonPath("$.name", is("Test User " + uniqueId)))
                .andExpect(jsonPath("$.email", is("testuser" + uniqueId + "@example.com")))
                .andExpect(jsonPath("$.isAdmin", is(false)));
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        String newUserRequestBody = String.format("""
            {
                "name": "Test User %s",
                "email": "testuser%s@example.com",
                "isAdmin": false
            }
            """, uniqueId, uniqueId);

        var post = MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserRequestBody);

        var result = mockMvc.perform(post)
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseBody);
        Long userId = root.path("id").asLong();
        System.out.println("Created User ID: " + userId);

        var delete = MockMvcRequestBuilders.delete("/api/users/" + userId);

        mockMvc.perform(delete)
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + userId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetUserById() throws Exception {

        Long userId = "user_2jdWFBtPvXcKnQABJ20JnxrBsYh";
        String uniqueId = ;
        var get = MockMvcRequestBuilders.get("/api/users/" + userId);

        mockMvc.perform(get)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.intValue())))
                .andExpect(jsonPath("$.name", is("Test User " + uniqueId)))
                .andExpect(jsonPath("$.email", is("testuser" + uniqueId + "@example.com")))
                .andExpect(jsonPath("$.isAdmin", is(false)));
    }*/
    }

