package org.example.groupbackend;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Before;
import org.example.groupbackend.products.Product;
import org.example.groupbackend.products.ProductDbRepo;
import org.example.groupbackend.request.*;
import org.example.groupbackend.user.User;
import org.example.groupbackend.user.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    HttpServletRequest request;

    @Autowired
    @InjectMocks
    RequestController controller;
    @Autowired
    @InjectMocks
    RequestService service;

    RequestProduct requestProduct = Mockito.mock(RequestProduct.class);

    @BeforeEach
    void setup() {
        Mockito.when(user.getIsAdmin()).thenReturn(false);

        Product product = Mockito.mock(Product.class);
        Mockito.when(requestProduct.getProduct()).thenReturn(product);
        Mockito.when(product.getName()).thenReturn("Stinker's Snake Oil");
        Mockito.when(productRepo.findAll()).thenReturn(List.of(product));
        Mockito.when(productRepo.getByName(anyString())).thenReturn(Optional.of(product));
        Mockito.when(requestRepo.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        Mockito.when(request.getAttribute("user")).thenReturn(user);
    }

    @Nested
    public class RegularUser {

        @Test
        public void canCreateNewRequest() throws Exception {

            RequestListDto dto = new RequestListDto(List.of(requestProduct), 1L);

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
            Mockito.when(requestRepo.findById(any())).thenReturn(Optional.of(rRequest));

            RequestApprovalDto dto = new RequestApprovalDto(rRequest.getId(), true);

            var response = controller.approveRequest(dto, request);
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    @Nested
    public class AdminUser extends RegularUser {

        @BeforeEach
        void setup() {
            Mockito.when(user.getIsAdmin()).thenReturn(true);
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
            Mockito.when(requestRepo.findById(any())).thenReturn(Optional.of(rRequest));

            RequestApprovalDto dto = new RequestApprovalDto(rRequest.getId(), true);

            var response = controller.approveRequest(dto, request);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }
}
