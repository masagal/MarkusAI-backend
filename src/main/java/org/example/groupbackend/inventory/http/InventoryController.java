package org.example.groupbackend.inventory.http;

import org.example.groupbackend.inventory.http.dto.InventoryItemDto;
import org.example.groupbackend.inventory.http.dto.UpdateInventoryItemDto;
import org.example.groupbackend.inventory.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(InventoryController.ENDPOINT)
public class InventoryController {
    public static final String ENDPOINT = "/inventory";
    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<InventoryItemDto>> getAllItemsInInventory() {
        List<InventoryItemDto> list = service.getAll()
                .stream()
                .map(InventoryItemDto::getDto)
                .toList();

        return ResponseEntity.ok(list);
    }

    @PatchMapping
    public ResponseEntity<Void> updateItem(@RequestBody UpdateInventoryItemDto dto) {
        service.updateQuantity(service.findById(dto.id()), dto.newQuantity());
        return ResponseEntity.accepted().build();
    }
}
