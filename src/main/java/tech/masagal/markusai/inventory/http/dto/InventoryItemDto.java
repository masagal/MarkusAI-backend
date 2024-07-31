package tech.masagal.markusai.inventory.http.dto;

import tech.masagal.markusai.inventory.model.InventoryItem;

public record InventoryItemDto(String id, String name, String quantity, String location, String imageUrl) {
    public static InventoryItemDto getDto(InventoryItem item) {
        return new InventoryItemDto(item.getId(), item.getLabel(), item.getQuantity().toString(), item.getLocation(), item.getImageUrl());
    }
}
