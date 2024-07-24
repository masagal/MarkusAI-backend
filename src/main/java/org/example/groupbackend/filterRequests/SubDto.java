package org.example.groupbackend.filterRequests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public record SubDto(String sub) {
}
