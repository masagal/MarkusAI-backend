package tech.masagal.markusai.inventory.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.masagal.markusai.inventory.model.InventoryDbRepo;
import tech.masagal.markusai.inventory.model.InventoryItem;
import tech.masagal.markusai.products.Product;
import tech.masagal.markusai.products.ProductDbRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InventoryService {
    Logger logger = LogManager.getLogger();

    InventoryDbRepo inventoryRepo;
    ProductDbRepo productRepo;

    public InventoryService(InventoryDbRepo inventoryRepo, ProductDbRepo productRepo) {
        this.inventoryRepo = inventoryRepo;
        this.productRepo = productRepo;
    }

    public InventoryItem createItem(String label, Integer quantity) {
        Product product = productRepo.getByName(label).orElse(new Product(label));
        InventoryItem item = new InventoryItem(product, quantity);
        inventoryRepo.save(item);
        return item;
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

    public void deleteProduct(InventoryItem item) {
        inventoryRepo.delete(item);
    }
}
