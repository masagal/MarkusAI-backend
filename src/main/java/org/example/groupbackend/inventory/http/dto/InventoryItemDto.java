package org.example.groupbackend.inventory.http.dto;

import org.example.groupbackend.inventory.model.InventoryItem;

public record InventoryItemDto(String id, String name, String quantity) {
    public static InventoryItemDto getDto(InventoryItem item) {
        return new InventoryItemDto(item.getId(), item.getLabel(), item.getQuantity().toString());
    }
}
