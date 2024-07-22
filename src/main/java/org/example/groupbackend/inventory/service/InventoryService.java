package org.example.groupbackend.inventory.service;

import org.example.groupbackend.inventory.model.InventoryDbRepo;
import org.example.groupbackend.inventory.model.InventoryItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    InventoryDbRepo inventoryRepo;

    public InventoryService(InventoryDbRepo inventoryRepo) {
        this.inventoryRepo = inventoryRepo;
    }

    public List<InventoryItem> getAll() {
        return inventoryRepo.findAll();
    }

}
