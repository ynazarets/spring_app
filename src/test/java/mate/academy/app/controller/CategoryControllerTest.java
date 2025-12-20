package mate.academy.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mate.academy.app.dto.category.CategoryDto;
import mate.academy.app.dto.category.CreateCategoryRequestDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
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
                    new ClassPathResource("database/categories/add-three-default-categories.sql",
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
                    new ClassPathResource("database/categories/remove-all-categories.sql",
                            BookControllerTest.class.getClassLoader())
            );
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("""
            Create a new category - Should return 201 Created and valid CategoryDto
            """)
    @Sql(
            scripts = "classpath:database/categories/delete-test-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void createCategory_ValidCategoryDto_ShouldSaveCategory() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("NameTest");

        CategoryDto expectedDto = new CategoryDto();
        expectedDto.setName(requestDto.getName());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(expectedDto, actual, "id"));
    }

    @Test
    @DisplayName("""
            Find all categories - Should return a paginated and sorted page of CategoryDto
            """)
    public void getAll_validRequest_ShouldReturnPageOfCategoryDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/categories")
                                .param("page", "0")
                                .param("size", "10")
                                .param("sort", "name,asc")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();

        com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        com.fasterxml.jackson.databind.JsonNode contentNode = jsonNode.get("content");

        List<CategoryDto> actual = objectMapper.readValue(contentNode.traverse(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, CategoryDto.class));
        assertEquals(3, actual.size());
        assertEquals("Category1", actual.get(0).getName());
        assertNotNull(actual.get(2).getId());
    }

    @Test
    @DisplayName("""
            Get category by existing ID - Should return valid CategoryDto
            """)
    public void getById_ValidId_ShouldReturnCategoryDto() throws Exception {
        Long RequestId = 1L;
        CategoryDto expectedDto = new CategoryDto();
        expectedDto.setId(RequestId);
        expectedDto.setName("Category1");

        MvcResult result = mockMvc.perform(
                        get("/categories/" + RequestId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actualDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class
        );
        assertNotNull(actualDto);
        assertTrue(EqualsBuilder.reflectionEquals(expectedDto, actualDto, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("""
            Update existing category - Should return 201 Created and updated CategoryDto
            """)
    public void updateCategory_ValidId_ShouldUpdateCategory() throws Exception {
        Long requestId = 1L;

        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("TestName2");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        put("/categories/" + requestId)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actualDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class
        );

        assertEquals("TestName2", actualDto.getName());
        assertEquals(requestId, actualDto.getId());
    }
}
