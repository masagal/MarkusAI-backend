package org.example.groupbackend.inventory.model;

import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.groupbackend.products.Product;

@Entity
@Table(name="inventory_items")
public class InventoryItem {
    @Transient
    Logger logger = LogManager.getLogger();

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    Product product;

    Integer quantity;

    protected InventoryItem() {
        logger.warn("Empty inventory item created.");
    }

    public InventoryItem(Product product) {
        this.product = product;
        this.quantity = 0;
    }

    public InventoryItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return product.getName();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
