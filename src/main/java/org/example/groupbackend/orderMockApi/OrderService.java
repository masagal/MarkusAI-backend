package org.example.groupbackend.orderMockApi;

import org.example.groupbackend.request.RequestService;
import org.example.groupbackend.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepo;

    public OrderService(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    public List<Order> getAllOrders(User user) {
        if(!user.getIsAdmin()) {
            throw new RequestService.NotAuthorizedException();
        }
        return orderRepo.findAll();
    }
}
