package org.example.groupbackend.orderMockApi;

import jakarta.persistence.*;
import org.example.groupbackend.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; //which admin user approved the order

    private OrderStatus status;

    private LocalDateTime approvedDate;

    public User getUser() {
        return user;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getApprovedDate() {
        return approvedDate;
    }
}
