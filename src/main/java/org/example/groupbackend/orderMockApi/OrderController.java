package org.example.groupbackend.orderMockApi;

import jakarta.servlet.ServletRequest;
import org.example.groupbackend.request.RequestApprovalDto;
import org.example.groupbackend.request.RequestService;
import org.example.groupbackend.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<Order> createNewOrder(@RequestBody OrderDto orderDto, ServletRequest req) {
        User user = (User) req.getAttribute("user");
        Order order = new Order(orderDto.status(), orderDto.approvedDate());
        Order createdOrder = orderService.createNewOrder(user, order, orderDto.requestId());
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(ServletRequest req) {
        User user = (User) req.getAttribute("user");
        List<Order> orders = orderService.getAllOrders(user);
        return ResponseEntity.ok(orders);
    }

    @PatchMapping
    public ResponseEntity<Void> changeStatusOfOrder(@RequestBody OrderStatusDto orderStatusDto, ServletRequest req) {
        User user = (User) req.getAttribute("user");
        orderService.changeStatusOfOrder(user, orderStatusDto.orderId(), orderStatusDto.orderStatus());
        return ResponseEntity.ok().build();
    }

}
