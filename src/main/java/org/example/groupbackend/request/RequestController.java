package org.example.groupbackend.request;

import jakarta.servlet.ServletRequest;
import org.example.groupbackend.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Request> postNewRequest(@RequestBody RequestListDto requestListDto, ServletRequest req) {
        User user = (User) req.getAttribute("user");

        Request postRequest = requestService.newRequestWithProducts(user, requestListDto.requests());
        return ResponseEntity.ok(postRequest);
    }

    @GetMapping
    public ResponseEntity<List<Request>> getAllRequests(ServletRequest req) {
        User user = (User) req.getAttribute("user");
        List<Request> requests = requestService.getAllRequests(user);
        return ResponseEntity.ok(requests);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ResponseEntity<Void> approveRequest(@RequestBody RequestApprovalDto requestApprovalDto, ServletRequest req) {
        User user = (User) req.getAttribute("user");

        try {
            requestService.approveRequest(user, requestApprovalDto.requestId(), requestApprovalDto.approve());
        } catch(RequestService.NotAuthorizedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }


}
