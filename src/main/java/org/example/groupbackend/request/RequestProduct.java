package org.example.groupbackend.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import org.example.groupbackend.products.Product;

@Entity
@Table(name = "request_products")
public class RequestProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Min(value = 0)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;

    public RequestProduct() {
    }

    public RequestProduct(Product product, int quantity, Request request) {
        this.product = product;
        this.quantity = quantity;
        this.request = request;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getId() {
        return id;
    }

}
