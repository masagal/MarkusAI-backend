package org.example.groupbackend.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ClerkService {

    private final RestTemplate restTemplate;
    private final String clerkApiUrl;
    private final String clerkApiKey;

    public ClerkService(RestTemplate restTemplate, @Value("${clerk.api.url}") String clerkApiUrl, @Value("${clerk.api.key}") String clerkApiKey) {
        this.restTemplate = restTemplate;
        this.clerkApiUrl = clerkApiUrl;
        this.clerkApiKey = clerkApiKey;
    }

    public UserDto createUser(UserDto userDto) {
        String url = clerkApiUrl + "/users";

        Map<String, Object> request = new HashMap<>();
        request.put("first_name", userDto.getName());
        request.put("email_address", new String[]{userDto.getEmail()});
        request.put("username", userDto.getName());
        request.put("password", "defaultPassword"); //generate random password

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(clerkApiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map responseBody = response.getBody();
            userDto.setId(Long.parseLong(responseBody.get("id").toString()));
            return userDto;
        } else {
            throw new RuntimeException("Failed to create user in Clerk");
        }
    }

    public UserDto getUser(String userId) {
        String url = clerkApiUrl + "/users/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(clerkApiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<UserDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, UserDto.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to retrieve user from Clerk");
        }
    }

    public void deleteUser(String userId) {
        String url = clerkApiUrl + "/users/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(clerkApiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to delete user from Clerk");
        }
    }
}
