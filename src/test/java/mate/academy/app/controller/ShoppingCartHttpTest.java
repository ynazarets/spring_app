package mate.academy.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mate.academy.app.dto.cartitem.CreateCartItemRequestDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartHttpTest {

    protected static MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

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
                    new ClassPathResource("database/shoppingCart/add-user-and-shopping-cart.sql",
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
                    new ClassPathResource("database/shoppingCart/remove-user-and-shopping-cart.sql",
                            BookControllerTest.class.getClassLoader())
            );
        }
    }

    @DisplayName("""
            Get shopping cart: should return cart for authenticated user
            """)
    @WithUserDetails("John@gmail.com")
    @Test
    public void getShoppingCart_validUserId_ShouldReturnShoppingCart() throws Exception {
        mockMvc.perform(
                get("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.cartItems").isNotEmpty());
    }

    @DisplayName("""
            Add book to cart: should add a new item and return updated cart with all items
            """)
    @WithUserDetails("John@gmail.com")
    @Test
    public void addBookToCart_validUserId_ShouldReturnUpdatedShoppingCart() throws Exception {

        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto();
        requestDto.setBookId(2L);
        requestDto.setQuantity(3);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(
                post("/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.cartItems", hasSize(2)))
                .andExpect(jsonPath("$.cartItems[?(@.bookId==2)].quantity").value(3))
                .andExpect(jsonPath("$.cartItems[?(@.bookId==1)].quantity").value(1));
    }
}
