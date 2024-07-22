package org.example.groupbackend.request.classesForTesting;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserTestClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public UserTestClass() {
    }

    public UserTestClass(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
