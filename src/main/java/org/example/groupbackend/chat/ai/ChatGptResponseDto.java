package org.example.groupbackend.chat.ai;

import org.example.groupbackend.chat.ChatMessage;

import java.util.List;

public record ChatGptResponseDto(String id, String object, String created, String model, List<ChoicesDto> choices, UsageDataDto usage, String systemFingerprint) {
    public record ChoicesMessageDto(String role, String content) {}
    public record ChoicesDto(String index, ChoicesMessageDto message, String logprobs, String finish_reason) {}
    public record UsageDataDto(Integer promptTokens, Integer completionTokens, Integer totalTokens) {}
}
