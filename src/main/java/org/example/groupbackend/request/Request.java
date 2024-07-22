package org.example.groupbackend.request;

import jakarta.persistence.*;
import org.example.groupbackend.products.Product;
import org.example.groupbackend.request.classesForTesting.UserTestClass;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserTestClass userTest;

    @OneToMany(mappedBy = "request", cascade ={ CascadeType.MERGE, CascadeType.REMOVE })
    private List<RequestProduct> products;

    public Request() {
        this.products = new ArrayList<>();
    }

    public void setUserTest(UserTestClass userTest) {
        this.userTest = userTest;
    }

    public List<RequestProduct> getProducts() {
        return products;
    }

    public void setProducts(List<RequestProduct> products) {
        this.products = products;
    }

    public Long getId() {
        return id;
    }
}
