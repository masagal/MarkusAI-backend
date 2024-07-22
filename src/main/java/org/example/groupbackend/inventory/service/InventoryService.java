package org.example.groupbackend.inventory.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.groupbackend.inventory.model.InventoryDbRepo;
import org.example.groupbackend.inventory.model.InventoryItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InventoryService {
    Logger logger = LogManager.getLogger();

    InventoryDbRepo inventoryRepo;

    public InventoryService(InventoryDbRepo inventoryRepo) {
        this.inventoryRepo = inventoryRepo;
    }

    public void createItem(String label, Integer quantity) {
        InventoryItem item = new InventoryItem(label, quantity);
        inventoryRepo.save(item);
    }

    public List<InventoryItem> getAll() {
        return inventoryRepo.findAll();
    }

    public InventoryItem findById(String id) {
        return inventoryRepo.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public void updateQuantity(InventoryItem item, Integer newQuantity) {
        if(newQuantity < 0) {
            logger.error("Attempted to set new quantity to a negative number.");
            throw new IllegalArgumentException("New quantity cannot be negative.");
        }
        item.setQuantity(newQuantity);
        inventoryRepo.save(item);
    }

}
