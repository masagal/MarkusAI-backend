package org.example.groupbackend.request;

import org.example.groupbackend.products.Product;
import org.example.groupbackend.products.ProductDbRepo;
import org.example.groupbackend.request.classesForTesting.UserRepoTest;
import org.example.groupbackend.request.classesForTesting.UserTestClass;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    private final RequestRepository requestRepo;
    private final ProductDbRepo productDbRepo;
    private final UserRepoTest userRepoTest;

    public RequestService(RequestRepository requestRepo, ProductDbRepo productDbRepo, UserRepoTest userRepoTest) {
        this.requestRepo = requestRepo;
        this.productDbRepo = productDbRepo;
        this.userRepoTest = userRepoTest;
    }

    public Request createNewRequest() {
        return requestRepo.save(new Request());
    }

    public Request newRequestWithProducts(List<RequestProduct> requestProds, Long userId) {
        UserTestClass testUser = userRepoTest.findById(userId).get();
        Request newRequest = createNewRequest();

        requestProds.forEach(prod -> {
            Product product = productDbRepo.getByName(prod.getProduct().getName());
            RequestProduct re = new RequestProduct(product, prod.getQuantity(), newRequest);
            newRequest.getProducts().add(re);
        });

        newRequest.setUserTest(testUser);
        return requestRepo.save(newRequest);
    }

    public List<Request> getAllRequests() {
        return requestRepo.findAll();
    }

    public void deleteRequest(Long id) {
        requestRepo.deleteById(id);
    }
}
