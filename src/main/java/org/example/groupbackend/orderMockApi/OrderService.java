package org.example.groupbackend.orderMockApi;

import org.example.groupbackend.request.Request;
import org.example.groupbackend.request.RequestRepository;
import org.example.groupbackend.request.RequestService;
import org.example.groupbackend.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final RequestRepository requestRepo;

    public OrderService(OrderRepository orderRepo, RequestRepository requestRepo) {
        this.orderRepo = orderRepo;
        this.requestRepo = requestRepo;
    }

    public Order createNewOrder(User user, Order order, Long requestId) {
        if(!user.getIsAdmin()) {
            throw new RequestService.NotAuthorizedException();
        }
        Request request = requestRepo.findById(requestId).orElseThrow();
        order.setApprovingAdminUser(user);
        order.setRequest(request);
        return orderRepo.save(order);
    }

    public List<Order> getAllOrders(User user) {
        return orderRepo.findAll();
    }

    public void changeStatusOfOrder(User user, Long orderId, OrderStatus orderStatus) {
        if(!user.getIsAdmin()) {
            throw new RequestService.NotAuthorizedException();
        }
        Order order = orderRepo.findById(orderId).orElseThrow(NoSuchElementException::new);
        order.setStatus(orderStatus);
        orderRepo.save(order);
    }
}
