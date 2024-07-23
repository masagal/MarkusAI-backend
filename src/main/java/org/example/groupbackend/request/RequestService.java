package org.example.groupbackend.request;

import org.example.groupbackend.products.Product;
import org.example.groupbackend.products.ProductDbRepo;
import org.example.groupbackend.user.User;
import org.example.groupbackend.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RequestService {

    private final RequestRepository requestRepo;
    private final ProductDbRepo productDbRepo;
    private final UserRepository userRepo;

    public RequestService(RequestRepository requestRepo, ProductDbRepo productDbRepo, UserRepository userRepo) {
        this.requestRepo = requestRepo;
        this.productDbRepo = productDbRepo;
        this.userRepo = userRepo;
    }

    public Request createNewRequest() {
        return requestRepo.save(new Request());
    }

    public Request newRequestWithProducts(List<RequestProduct> requestProds, Long userId) {
        User user = userRepo.findById(userId).orElseThrow();
        Request newRequest = createNewRequest();

        addRequestProductsToRequest(requestProds, newRequest);
        newRequest.setUserTest(user);

        return requestRepo.save(newRequest);
    }

    public List<Request> getAllRequests() {
        return requestRepo.findAll();
    }

    public void deleteRequest(Long id) {
        requestRepo.findById(id).orElseThrow();
        requestRepo.deleteById(id);
    }

    public void approveRequest(Long requestId, boolean approve) {
        Request request = requestRepo.findById(requestId).orElseThrow(NoSuchElementException::new);
        request.setApproved(approve);
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
