package org.example.groupbackend;

import org.example.groupbackend.user.User;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestUserConfig {
    User user = new User();

    @Bean
    public User getUser() {
        return user;
    }
}
