package org.example.groupbackend.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ClerkService {

    @Value("${clerk.api.url}")
    private String clerkApiUrl;

    @Value("${clerk.api.key}")
    private String clerkApiKey;

    private final RestTemplate restTemplate;

    public ClerkService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDto createUser(UserDto userDto) {
        String url = clerkApiUrl + "/users";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + clerkApiKey);
        headers.set("Content-Type", "application/json");

        String username = userDto.getName().replaceAll("[^a-zA-Z0-9_-]", "_");

        String requestBody = String.format("{\"first_name\":\"%s\", \"email_address\":[\"%s\"], \"username\":\"%s\", \"password\":\"%s\"}",
                userDto.getName(), userDto.getEmail(), username, "secureRandomPassword");

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                UserDto createdUser = new UserDto();
                createdUser.setId(root.path("id").asLong());
                createdUser.setName(root.path("first_name").asText());
                createdUser.setEmail(root.path("email_addresses").get(0).path("email_address").asText());
                createdUser.setIsAdmin(userDto.getIsAdmin());
                return createdUser;
            } else {
                throw new RuntimeException("Failed to create user: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to create user: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response from Clerk: " + e.getMessage(), e);
        }
    }

    public void deleteUser(String clerkUserId) {
        String url = clerkApiUrl + "/users/" + clerkUserId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + clerkApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to delete user: " + response.getStatusCode());
        }
    }
}
