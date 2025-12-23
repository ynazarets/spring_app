package mate.academy.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mate.academy.app.dto.order.OrderItemResponseDto;
import mate.academy.app.dto.order.PlaceOrderRequestDto;
import mate.academy.app.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.app.model.Order;
import mate.academy.app.model.OrderItem;
import mate.academy.app.model.enums.OrderStatus;
import mate.academy.app.repository.OrderRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderHttpTest {

    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderRepository orderRepository;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext,
            @Autowired DataSource dataSource
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/order/add-default-order.sql",
                            BookControllerTest.class.getClassLoader())
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    private static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/order/remove-all-order.sql",
                            BookControllerTest.class.getClassLoader())
            );
        }
    }

    @DisplayName("""
            Create order: should save order with correct address and status
            """)
    @WithUserDetails("John@gmail.com")
    @Test
    public void createOrder_ValidRequest_ShouldCreateNewOrder() throws Exception {
        PlaceOrderRequestDto requestDto = new PlaceOrderRequestDto();
        requestDto.setShippingAddress("newAddress");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/orders")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Order> orders = orderRepository.findAllByUserIdWithItemsAndBooks(1L, Pageable.unpaged())
                .getContent();

        assertFalse("Order should be saved in the database", orders.isEmpty());
        Order actualOrder = orders.stream()
                .filter(o -> "newAddress".equals(o.getShippingAddress()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Order with address 'newAddress' not found in DB"));
        assertEquals(requestDto.getShippingAddress(), actualOrder.getShippingAddress());
        assertEquals("PENDING", actualOrder.getOrderStatus().name());
    }

    @DisplayName("""
            Update order status: should successfully update status for order 100
            """)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void updateOrderStatus_ValidId_ShouldUpdateOrderStatus() throws Exception {
        Long orderId = 100L;
    UpdateOrderStatusRequestDto requestDto = new UpdateOrderStatusRequestDto();
    requestDto.setStatus(OrderStatus.valueOf("DELIVERED"));
    String jsonRequest = objectMapper.writeValueAsString(requestDto);

    MvcResult result = mockMvc.perform(patch("/orders/" + orderId)
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        Order actualOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new AssertionError("Order 100 not found in DB"));
        assertEquals(OrderStatus.DELIVERED.name(), actualOrder.getOrderStatus().name());
    }

    @DisplayName("""
            Get order item by ID: should return correct item for authorized user
            """)
    @WithUserDetails("John@gmail.com")
    @Test
    public void getOrderItemById_ValidId_ShouldReturnOrderItemDto() throws Exception {
        Long userId = 1L;
        Long orderId = 100L;
        Long itemId = 100L;

        MvcResult result = mockMvc.perform(get("/orders/{orderId}/items/{itemId}", orderId, itemId )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId))
                .andExpect(jsonPath("$.bookId").exists())
                .andReturn();

        Optional<OrderItem> actual = orderRepository.findOrderItemByIdAndOrderIdAndOrderUserId(itemId, orderId, userId);
        assertTrue("OrderItem should exist in DB", actual.isPresent());
        assertEquals(itemId, actual.get().getId());
    }
}
