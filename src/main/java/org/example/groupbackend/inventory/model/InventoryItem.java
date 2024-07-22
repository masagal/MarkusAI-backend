package org.example.groupbackend.inventory.model;

import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Entity
@Table(name="inventory_items")
public class InventoryItem {
    @Transient
    Logger logger = LogManager.getLogger();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String id;

    String label;

    Integer quantity;

    protected InventoryItem() {
        logger.warn("Empty inventory item created.");
    }

    public InventoryItem(String label) {
        this.label = label;
        this.quantity = 0;
    }

    public InventoryItem(String label, Integer quantity) {
        this.label = label;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
