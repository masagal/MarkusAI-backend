package tech.masagal.markusai.orderMockApi;

import jakarta.persistence.*;
import tech.masagal.markusai.request.Request;
import tech.masagal.markusai.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User approvingAdminUser; //which admin user approved the order

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

    public User getApprovingAdminUser() {
        return approvingAdminUser;
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

    public void setApprovingAdminUser(User user) {
        this.approvingAdminUser = user;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Long getId() {
        return id;
    }
}
