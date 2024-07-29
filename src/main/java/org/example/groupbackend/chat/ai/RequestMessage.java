package org.example.groupbackend.chat.ai;

import org.example.groupbackend.chat.ChatMessage;
import org.example.groupbackend.request.Request;

public record RequestMessage(String message, Request request) {}
