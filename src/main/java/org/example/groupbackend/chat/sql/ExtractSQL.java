package org.example.groupbackend.chat.sql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public record ExtractSQL(String sqlStatement) {
}
