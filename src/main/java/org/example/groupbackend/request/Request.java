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

    @OneToMany(mappedBy = "request", cascade ={ CascadeType.MERGE, CascadeType.REMOVE })
    private List<RequestProduct> products;

    public Request() {
        this.products = new ArrayList<>();
    }

    public void setUserTest(User user) {
        this.user = user;
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
