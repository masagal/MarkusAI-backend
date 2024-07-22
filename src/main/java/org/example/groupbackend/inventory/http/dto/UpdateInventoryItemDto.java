package org.example.groupbackend.inventory.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateInventoryItemDto(String id, String label, @JsonProperty(value = "new_quantity") Integer newQuantity) {
}
