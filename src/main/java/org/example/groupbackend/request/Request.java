package org.example.groupbackend.request;

import jakarta.persistence.*;
import org.example.groupbackend.user.User;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RequestProduct> products;

    private boolean isApproved;

    public Request() {
        this.products = new ArrayList<>();
        this.isApproved = false;
    }

    public Request(User user) {
        this();
        this.user = user;
    }

    public void setUserTest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
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

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean isApproved() {
        return isApproved;
    }
}
