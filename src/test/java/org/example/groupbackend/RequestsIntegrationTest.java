package org.example.groupbackend;

import org.aspectj.lang.annotation.Before;
import org.example.groupbackend.products.Product;
import org.example.groupbackend.products.ProductDbRepo;
import org.example.groupbackend.request.*;
import org.example.groupbackend.user.User;
import org.example.groupbackend.user.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

    @Autowired
    @InjectMocks
    RequestController controller;
    @Autowired
    @InjectMocks
    RequestService service;

    RequestProduct requestProduct = Mockito.mock(RequestProduct.class);

    @Nested
    public class RegularUser {

        @BeforeEach
        void setup() {
            Mockito.when(user.getIsAdmin()).thenReturn(false);

            Product product = Mockito.mock(Product.class);
            Mockito.when(requestProduct.getProduct()).thenReturn(product);
            Mockito.when(product.getName()).thenReturn("Stinker's Snake Oil");
            Mockito.when(productRepo.findAll()).thenReturn(List.of(product));
            Mockito.when(productRepo.getByName(anyString())).thenReturn(Optional.of(product));
            Mockito.when(requestRepo.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        }

        @Test
        public void canCreateNewRequest() throws Exception {
            RequestListDto dto = new RequestListDto(List.of(requestProduct), 1L);

            var response = controller.postNewRequest(dto);

            assertEquals(response.getStatusCode(), HttpStatus.OK);
        }

        @Test
        public void canOnlySeeTheirOwnRequests() {
            fail();
        }

        @Test
        public void cannotApproveRequest() {
            Request request = new Request(user);
            request.setProducts(List.of(requestProduct));
            Mockito.when(requestRepo.findById(any())).thenReturn(Optional.of(request));
            RequestApprovalDto dto = new RequestApprovalDto(request.getId(), true);

            var response = controller.approveRequest(dto);
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    public class AdminUser {
        public void canCreateNewRequest() {
            fail();
        }

        public void canSeeAnyRequest() {

        }

        public void canApproveRequest() {
            fail();
        }
    }
}
