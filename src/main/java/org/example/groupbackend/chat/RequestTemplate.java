package org.example.groupbackend.chat;

import java.util.List;

public record RequestTemplate(List<String> template, String request) {
}
