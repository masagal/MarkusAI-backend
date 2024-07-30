package tech.masagal.markusai;

import jakarta.servlet.http.HttpServletRequest;
import tech.masagal.markusai.orderMockApi.OrderService;
import tech.masagal.markusai.products.Product;
import tech.masagal.markusai.products.ProductDbRepo;
import org.example.groupbackend.request.*;
import tech.masagal.markusai.request.*;
import tech.masagal.markusai.user.User;
import tech.masagal.markusai.user.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RequestsIntegrationTest {

    @MockBean
    User user;

    @MockBean
    UserRepository userRepo;
    @MockBean
    RequestRepository requestRepo;
    @MockBean
    ProductDbRepo productRepo;
    @MockBean
    OrderService orderService;
    @MockBean
    HttpServletRequest request;

    @Autowired
    @InjectMocks
    RequestController controller;
    @Autowired
    @InjectMocks
    RequestService service;

    RequestProduct requestProduct = Mockito.mock(RequestProduct.class);
    RequestProductDto requestProductDto = Mockito.mock(RequestProductDto.class);

    @BeforeEach
    void setup() {
        when(user.getIsAdmin()).thenReturn(false);

        Product product = Mockito.mock(Product.class);
        when(requestProduct.getProduct()).thenReturn(product);
        when(product.getName()).thenReturn("Stinker's Snake Oil");
        when(productRepo.findAll()).thenReturn(List.of(product));
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(productRepo.getByName(anyString())).thenReturn(Optional.of(product));
        when(requestRepo.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(request.getAttribute("user")).thenReturn(user);
    }

    @Nested
    public class RegularUser {

        @Test
        public void canCreateNewRequest() throws Exception {
            when(requestProductDto.productId()).thenReturn("1");
            RequestListDto dto = new RequestListDto(List.of(requestProductDto), 1L);

            var response = controller.postNewRequest(dto, request);

            assertEquals(response.getStatusCode(), HttpStatus.OK);
        }

        @Test
        public void canOnlySeeTheirOwnRequests() {
            controller.getAllRequests(request);

            verify(requestRepo).findAllByUser(user);
        }

        @Test
        public void cannotApproveRequest() {
            Request rRequest = new Request(user);
            rRequest.setProducts(List.of(requestProduct));
            when(requestRepo.findById(any())).thenReturn(Optional.of(rRequest));

            RequestApprovalDto dto = new RequestApprovalDto(rRequest.getId(), true);

            var response = controller.approveRequest(dto, request);
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    @Nested
    public class AdminUser extends RegularUser {

        @BeforeEach
        void setup() {
            when(user.getIsAdmin()).thenReturn(true);
        }

        @Test
        @Override
        public void canOnlySeeTheirOwnRequests() {
            controller.getAllRequests(request);

            verify(requestRepo).findAll();
            Mockito.verify(requestRepo, never()).findAllByUser(user);
        }

        @Test
        @Disabled
        @Override
        public void cannotApproveRequest() {
        }

        @Test
        public void canApproveRequest() {
            Request rRequest = new Request(user);
            rRequest.setProducts(List.of(requestProduct));
            when(requestRepo.findById(any())).thenReturn(Optional.of(rRequest));

            RequestApprovalDto dto = new RequestApprovalDto(rRequest.getId(), true);

            var response = controller.approveRequest(dto, request);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }
}
