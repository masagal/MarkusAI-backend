package org.example.groupbackend.request;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseEntity<Request> postNewRequest(@RequestBody RequestListDto requestListDto) {
        var postRequest = requestService.newRequestWithProducts(requestListDto.requests(), requestListDto.userId());
        return ResponseEntity.ok(postRequest);
    }

    @GetMapping
    public ResponseEntity<List<Request>> getAllRequests() {
        var requests = requestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }


}
