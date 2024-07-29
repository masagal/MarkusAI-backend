package org.example.groupbackend.request;

import org.example.groupbackend.orderMockApi.Order;
import org.example.groupbackend.orderMockApi.OrderService;
import org.example.groupbackend.orderMockApi.OrderStatus;
import org.example.groupbackend.products.Product;
import org.example.groupbackend.products.ProductDbRepo;
import org.example.groupbackend.user.User;
import org.example.groupbackend.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RequestService {

    private final RequestRepository requestRepo;
    private final ProductDbRepo productDbRepo;
    private final OrderService orderService;

    public RequestService(RequestRepository requestRepo, ProductDbRepo productDbRepo, OrderService orderService) {
        this.requestRepo = requestRepo;
        this.productDbRepo = productDbRepo;
        this.orderService = orderService;
    }

    public Request createNewRequest(User user) {
        return requestRepo.save(new Request(user));
    }

    public Request newRequestWithProducts(User user, List<RequestProduct> requestProds) {
        Request newRequest = createNewRequest(user);

        addRequestProductsToRequest(requestProds, newRequest);
        newRequest.setUserTest(user);

        return requestRepo.save(newRequest);
    }

    public List<Request> getAllRequests(User user) {
        if(user.getIsAdmin()) {
            return requestRepo.findAll();
        }
        return requestRepo.findAllByUser(user);
    }

    public void deleteRequest(Long id) {
        requestRepo.findById(id).orElseThrow();
        requestRepo.deleteById(id);
    }

    static public class NotAuthorizedException extends IllegalArgumentException {

    }

    public void approveRequest(User user, Long requestId, boolean approve) {
        if(!user.getIsAdmin()) {
            throw new NotAuthorizedException();
        }
        Request request = requestRepo.findById(requestId).orElseThrow(NoSuchElementException::new);
        request.setApproved(approve);
        if (approve) {
            Order newOrder = new Order(OrderStatus.PENDING, LocalDateTime.now());
            orderService.createNewOrder(user, newOrder, requestId);
        }
        requestRepo.save(request);
    }

    private void addRequestProductsToRequest(List<RequestProduct> requestProds, Request request) {
        requestProds.forEach(prod -> {
            Product product = productDbRepo.getByName(prod.getProduct().getName()).orElseThrow(NoSuchElementException::new);
            if (product == null) {
                throw new NoSuchElementException("Product does not exist!");
            }
            RequestProduct reqProd = new RequestProduct(product, prod.getQuantity(), request);
            request.getProducts().add(reqProd);
        });
    }


}
