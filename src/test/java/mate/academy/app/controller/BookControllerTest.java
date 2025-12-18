package mate.academy.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mate.academy.app.dto.book.BookDto;
import mate.academy.app.dto.book.CreateBookRequestDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

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
                    new ClassPathResource("database/books/add-three-default-books.sql",
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
                    new ClassPathResource("database/books/remove-all-books.sql",
                            BookControllerTest.class.getClassLoader())
            );
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("""
            da
            """)
    @Sql(
            scripts = "classpath:database/books/delete-test-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void createBook_ValidRequest_ShouldCreateBook() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("TitleTest");
        requestDto.setAuthor("AuthorTest");
        requestDto.setIsbn("123456789");
        requestDto.setPrice(BigDecimal.valueOf(10.35));

        BookDto expectedDto = new BookDto();
        expectedDto.setTitle(requestDto.getTitle());
        expectedDto.setAuthor(requestDto.getAuthor());
        expectedDto.setIsbn(requestDto.getIsbn());
        expectedDto.setPrice(requestDto.getPrice());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                post("/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actualDto = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actualDto);
        Assertions.assertNotNull(actualDto.getId());
        EqualsBuilder.reflectionEquals(expectedDto, actualDto, "id");
    }

    @Test
    @DisplayName("""
            da
            """)
    public void findAll_ValidRequest_ShouldReturnPageOfBooksDDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/books")
                                .param("page", "0")
                                .param("size", "10")
                                .param("sort", "title,asc")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();

        com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        com.fasterxml.jackson.databind.JsonNode contentNode = jsonNode.get("content");

        List<BookDto> actualBooks = objectMapper.readValue(contentNode.traverse(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, BookDto.class));
        assertEquals(3, actualBooks.size());
        assertEquals("Book 1", actualBooks.get(0).getTitle());
    }

    @Test
    @DisplayName("""
            da
            """)
    public void getById_ValidId_ShouldReturnBookDto() throws Exception {
        Long requestId = 1L;

        BookDto expectedDto = new BookDto();
        expectedDto.setId(requestId);
        expectedDto.setTitle("Book 1");
        expectedDto.setAuthor("Author 1");
        expectedDto.setIsbn("123456789");
        expectedDto.setPrice(BigDecimal.valueOf(10.35));

        String jsonResponse = objectMapper.writeValueAsString(expectedDto);

        MvcResult result = mockMvc.perform(
                        get("/books/" + requestId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actualDto = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        EqualsBuilder.reflectionEquals(expectedDto, actualDto, "id");
    }

    @Test
    @DisplayName("""
            da
            """)
    public void search_ValidRequest_ShouldReturnListOfBooks() throws Exception {

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Book 2");
        requestDto.setAuthor("Author 2");

        String searchTitle = "Book 2";
        String searchAuthor = "Author 2";

        MvcResult result = mockMvc.perform(
                        get("/books/search")
                                .param("title", searchTitle)
                                .param("author", searchAuthor)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();

        List<BookDto> actualBooks = objectMapper.readValue(
                jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, BookDto.class)
        );
        assertNotNull(actualBooks);
        assertEquals(1, actualBooks.size());
        assertEquals(searchTitle, actualBooks.get(0).getTitle());
        assertEquals(searchAuthor, actualBooks.get(0).getAuthor());

    }
}



































