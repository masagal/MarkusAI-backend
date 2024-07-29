package org.example.groupbackend.chat.ai;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;

public record ChatResult (@JsonProperty("messageToUser") String chatMessage, Optional<ChatResultRequest> request) {
    public record ChatResultRequestProduct(@JsonProperty("product_id") Integer productId, Integer quantity) {}
    public record ChatResultRequest(List<ChatResultRequestProduct> products) {}
}
