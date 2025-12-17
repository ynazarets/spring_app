package mate.academy.app.repository;

import mate.academy.app.model.Book;
import mate.academy.app.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("""
            gf
            """)
    public void findAllByCategoryId_ValidCategoryId_ShouldReturnListOfBooks() {
        Category category = new Category();
        category.setName("Test");
        categoryRepository.save(category);

        Book book = new Book();
        book.setTitle("TitleTest");
        book.setAuthor("AuthorTest");
        book.setIsbn("123456789");
        book.setPrice(BigDecimal.valueOf(10.50));
        book.setCategories(Set.of(category));
        bookRepository.save(book);

        Page<Book> actual = bookRepository.findAllByCategoriesId(1L, Pageable.unpaged());
        assertEquals(1, actual.getContent().size());
    }
}
