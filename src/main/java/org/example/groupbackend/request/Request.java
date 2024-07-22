package org.example.groupbackend.request;

import jakarta.persistence.*;
import org.example.groupbackend.request.classesForTesting.UserTestClass;

@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @ManyToOne
    private UserTestClass userTest;

    public Request() {
    }

    public Request(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public UserTestClass getUserTest() {
        return userTest;
    }

    public void setUserTest(UserTestClass userTest) {
        this.userTest = userTest;
    }
}
