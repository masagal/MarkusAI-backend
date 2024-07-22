package org.example.groupbackend.request;

import jakarta.persistence.*;
import org.example.groupbackend.products.Product;


@Entity
public class RequestProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;

}
