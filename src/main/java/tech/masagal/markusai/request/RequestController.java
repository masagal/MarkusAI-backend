package tech.masagal.markusai.request;

import jakarta.servlet.ServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.masagal.markusai.products.ProductDbRepo;
import tech.masagal.markusai.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(RequestController.REQUEST_ENDPOINT)
@CrossOrigin
public class RequestController {
    Logger logger = LogManager.getLogger();
    public static final String REQUEST_ENDPOINT = "/requests";

    private final ProductDbRepo productRepo;
    private final RequestService requestService;

    public RequestController(RequestService requestService, ProductDbRepo productRepo) {
        this.requestService = requestService;
        this.productRepo = productRepo;
    }

    @PostMapping
    public ResponseEntity<Request> postNewRequest(@RequestBody RequestListDto requestListDto, ServletRequest req) {
        User user = (User) req.getAttribute("user");

        Request postRequest = requestService.newRequestWithProducts(user,
                requestListDto.requests().stream().map((dto) -> new RequestProduct(productRepo.findById(Long.valueOf(dto.productId())).orElseThrow(),
                        dto.quantity(),
                        null))
                        .toList());
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
        logger.info("approved request {} with approval {}", requestApprovalDto.requestId(), requestApprovalDto.approve());
        return ResponseEntity.ok().build();
    }


}
