package org.example.groupbackend.orderMockApi;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderControllerTest {

    @Value("${server.port}")
    private int port;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OrderRepository orderRepository;

    @MockBean
    HttpServletRequest request;

    private final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyXzJqbTZBWVRoQ0RmUHFNbEh" +
            "CTzhqaHd5UExNSSIsIm5hbWUiOiJNYXRoIG1hdGgiLCJlbWFpbCI6Im1hdGhpLmFkbWluQGdtYWlsLmNvbS" +
            "J9.V4J4EUnJjqIm95K5X0vBFX2xNt77Z63n4XXWge6_Vdw";


    @Test
    @Order(1)
    public void shouldGetListOfOrdersContainingTwoOrdersWithCorrectContent() throws Exception {
        String url = "http://localhost:%s/orders".formatted(port);

        mockMvc.perform(get(url).header(HttpHeaders.AUTHORIZATION, token))
                //.andDo(print()) // print response details
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].user.email").value("mathi.admin@gmail.com"))
                .andExpect(jsonPath("$.[0].status").value("PENDING"))
                .andExpect(jsonPath("$.[1].status").value("PENDING"))
                .andExpect(jsonPath("$.[0].request.products", hasSize(1)))
                .andExpect(jsonPath("$.[0].request.products.[0].product.name").value("Green whiteboard markers"))
                .andExpect(jsonPath("$.[1].request.products", hasSize(1)))
                .andExpect(jsonPath("$.[1].request.products.[0].product.name").value("Orange whiteboard markers"));
    }

    @Test
    @Order(2)
    public void shouldChangeStatusOfSecondOrderToARRIVED() throws Exception {
        String url = "http://localhost:%s/orders".formatted(port);
        String changeOrder = """
                   {
                  "orderId": 2,
                  "orderStatus": "ARRIVED"
                   }
                """;

        mockMvc.perform(patch(url).header(HttpHeaders.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON).content(changeOrder))
                .andDo(print()) // print response details
                .andExpect(status().isOk());

        assertThat(this.orderRepository.findAll()).hasSize(2);
        assertThat(this.orderRepository.findAll().get(1).getStatus()).isEqualTo(OrderStatus.ARRIVED);

    }

    @Test
    @Order(3)
    public void shouldPostNewOrder() throws Exception {
        String url = "http://localhost:%s/orders".formatted(port);
        String newOrder = """
                   {
                   "status": "PENDING",
                   "approvedDate": "2024-07-26T14:00:00",
                   "requestId": 4
                   }
                """;

        mockMvc.perform(post(url).header(HttpHeaders.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON).content(newOrder))
                .andDo(print()) // print response details
                .andExpect(status().isCreated());

        assertThat(this.orderRepository.findAll()).hasSize(3);
        assertThat(this.orderRepository.findById(3L)).isNotNull();
    }

    @Test
    @Order(4)
    public void nonAdminUserCannotChangeStatusToOrder() throws Exception {
        String url = "http://localhost:%s/orders".formatted(port);
        String changeOrder = """
                   {
                  "orderId": 1,
                  "orderStatus": "ARRIVED"
                   }
                """;

        String fakeToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyXzJqakl6R3E4UE82bW" +
                "tSYUY4YVZhYnA0T1ZYUiIsIm5hbWUiOiJOb25BZG1pbiBtYXRoIiwiZW1haWwiOiJhbHdheXN0aGVyZUBn" +
                "bWFpbC5jb20ifQ.q-_0qKSYUmobjy58TLu3yQ2ul2emzGWtSkb6u3PkoI8";

        mockMvc.perform(patch(url).header(HttpHeaders.AUTHORIZATION, fakeToken).contentType(MediaType.APPLICATION_JSON).content(changeOrder))
                .andDo(print()) // print response details
                .andExpect(status().isForbidden());

        assertThat(this.orderRepository.findAll().get(0).getStatus()).isEqualTo(OrderStatus.PENDING);

    }
}
