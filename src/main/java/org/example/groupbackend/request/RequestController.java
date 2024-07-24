package org.example.groupbackend.request;

import jakarta.servlet.ServletRequest;
import org.example.groupbackend.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(RequestController.REQUEST_ENDPOINT)
@CrossOrigin
public class RequestController {
    public static final String REQUEST_ENDPOINT = "/requests";

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseEntity<Request> postNewRequest(@RequestBody RequestListDto requestListDto) {
        Request postRequest = requestService.newRequestWithProducts(requestListDto.requests(), requestListDto.userId());
        return ResponseEntity.ok(postRequest);
    }

    @GetMapping
    public ResponseEntity<List<Request>> getAllRequests(User user) {
        List<Request> requests = requestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ResponseEntity<Void> approveRequest(@RequestBody RequestApprovalDto requestApprovalDto) {
        requestService.approveRequest(requestApprovalDto.requestId(), requestApprovalDto.approve());
        return ResponseEntity.ok().build();
    }


}
