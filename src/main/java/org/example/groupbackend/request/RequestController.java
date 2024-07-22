package org.example.groupbackend.request;

import org.example.groupbackend.request.classesForTesting.UserTestClass;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    /*@GetMapping
    private ResponseEntity<Request> getAllRequests() {
    }*/

    @PostMapping
    private ResponseEntity<Request> postNewRequest(@RequestBody RequestDto requestDto) {
        var postRequest = requestService.newRequest(new Request(requestDto.quantity()), requestDto.userId());
        return ResponseEntity.ok(postRequest);
    }
}
