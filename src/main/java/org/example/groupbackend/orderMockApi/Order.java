package org.example.groupbackend.orderMockApi;

import jakarta.persistence.*;
import org.example.groupbackend.request.Request;
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

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime approvedDate;

    @OneToOne
    @JoinColumn(name = "request_id")
    private Request request;

    public Order() {
    }

    public Order(OrderStatus status, LocalDateTime approvedDate) {
        this.status = status;
        this.approvedDate = approvedDate;
    }

    public User getUser() {
        return user;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getApprovedDate() {
        return approvedDate;
    }

    public Request getRequest() {
        return request;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Long getId() {
        return id;
    }
}
