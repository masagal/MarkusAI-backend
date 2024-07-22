package org.example.groupbackend.inventory.http;

import org.example.groupbackend.inventory.http.dto.InventoryItemDto;
import org.example.groupbackend.inventory.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
