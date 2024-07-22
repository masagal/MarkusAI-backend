package org.example.groupbackend.request;

import org.example.groupbackend.request.classesForTesting.UserTestClass;
import org.springframework.stereotype.Service;

@Service
public class RequestService {

    private final RequestRepository requestRepo;

    public RequestService(RequestRepository requestRepo) {
        this.requestRepo = requestRepo;
    }

    public Request newRequest(Request request, Long userId) {
        UserTestClass testUser = new UserTestClass("Test user");
        request.setUserTest(testUser);
        return requestRepo.save(request);
    }
}
