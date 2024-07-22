package org.example.groupbackend.request;

import jakarta.persistence.*;
import org.example.groupbackend.products.Product;
import org.example.groupbackend.request.classesForTesting.UserTestClass;

import java.util.List;


@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserTestClass userTest;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
    private List<RequestProduct> products;

    public Request() {
    }

    public Request(List<RequestProduct> products) {
        this.products = products;
    }

    public void setUserTest(UserTestClass userTest) {
        this.userTest = userTest;
    }
}
