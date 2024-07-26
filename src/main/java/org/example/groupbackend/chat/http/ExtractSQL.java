package org.example.groupbackend.chat.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public record ExtractSQL(String sqlStatement) {
}
