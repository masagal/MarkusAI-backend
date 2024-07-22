package org.example.groupbackend.request.classesForTesting;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ProductTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String prod_name;

    public ProductTest(String prod_name) {
        this.prod_name = prod_name;
    }

    public ProductTest() {
    }

    public String getProd_name() {
        return prod_name;
    }
}
