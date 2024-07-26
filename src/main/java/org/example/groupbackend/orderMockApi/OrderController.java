package org.example.groupbackend.orderMockApi;

import jakarta.servlet.ServletRequest;
import org.example.groupbackend.request.Request;
import org.example.groupbackend.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(OrderController.ORDER_ENDPOINT)
@CrossOrigin
public class OrderController {
    public static final String ORDER_ENDPOINT = "/orders";

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(ServletRequest req) {
        User user = (User) req.getAttribute("user");
        List<Order> orders = orderService.getAllOrders(user);
        System.out.println("Order are here: " + orders);
        return ResponseEntity.ok(orders);
    }
}
