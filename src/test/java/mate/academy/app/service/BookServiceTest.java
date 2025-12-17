package mate.academy.app.service;

import io.swagger.v3.oas.annotations.Operation;
import mate.academy.app.dto.book.BookDto;
import mate.academy.app.dto.book.CreateBookRequestDto;
import mate.academy.app.exception.EntityNotFoundException;
import mate.academy.app.mapper.BookMapper;
import mate.academy.app.mapper.CategoryMapperHelper;
import mate.academy.app.model.Book;
import mate.academy.app.repository.BookRepository;
import mate.academy.app.service.impl.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private CategoryMapperHelper categoryMapperHelper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("""
           
            """)
    public void save_ValidCreateBookRequestDto_ShouldReturnBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("TitleTest");
        requestDto.setAuthor("AuthorTest");
        requestDto.setIsbn("123456789");
        requestDto.setPrice(BigDecimal.valueOf(10.00));
        requestDto.setCategoryIds(List.of(1L));

        Book book = new Book();
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());
        book.setPrice(requestDto.getPrice());

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle(requestDto.getTitle());
        savedBook.setAuthor(requestDto.getAuthor());
        savedBook.setIsbn(requestDto.getIsbn());
        savedBook.setPrice(requestDto.getPrice());

        BookDto expectedDto = new BookDto();
        expectedDto.setId(1L);
        expectedDto.setTitle(savedBook.getTitle());
        expectedDto.setAuthor(savedBook.getAuthor());

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.toBookDto(savedBook)).thenReturn(expectedDto);
        when(categoryMapperHelper.mapCategoryIdsToCategory(requestDto.getCategoryIds()))
                .thenReturn(Collections.emptySet());
        BookDto actualDto = bookService.save(requestDto);
        assertEquals(expectedDto, actualDto);
    }
    @Test
    @DisplayName("""
            
            """)
    public void getById_ValidId_ShouldReturnBookDto() {
        Long RequestId = 1L;
        Book expectedBook = new Book();
        expectedBook.setId(RequestId);
        expectedBook.setTitle("TitleTest");
        expectedBook.setAuthor("AuthorTest");
        expectedBook.setIsbn("123456789");
        expectedBook.setPrice(BigDecimal.valueOf(10.00));

        BookDto expectedDto = new BookDto();
        expectedDto.setId(expectedBook.getId());
        expectedDto.setTitle(expectedBook.getTitle());
        expectedDto.setAuthor(expectedBook.getAuthor());

        when(bookRepository.findById(RequestId)).thenReturn(Optional.of(expectedBook));
        when(bookMapper.toBookDto(expectedBook)).thenReturn(expectedDto);

        BookDto actualDto = bookService.getById(RequestId);
        assertEquals(expectedDto, actualDto);
    }

    @Test
    @DisplayName("""
            
            """)
    public void findAll_ValidRequest_ShouldReturnListOfBookDto() {
        Pageable pageable = PageRequest.of(0, 10);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("TitleTest");
        book.setAuthor("AuthorTest");
        book.setIsbn("123456789");
        book.setPrice(BigDecimal.valueOf(10.00));

        List<Book> books = List.of(book);

        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);

        Page<BookDto> actualPage = bookService.findAll(pageable);
        assertEquals(1, actualPage.getContent().size());
        assertEquals(bookDto, actualPage.getContent().get(0));
        verify(bookRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("""
            
            """)
    public void deleteById_validId_successDeleted() {
        Long id = 1L;
        bookService.deleteById(id);
        verify(bookRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("""
            
            """)
    public void update_validId_successUpdated() {
        Long id = 1L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("TitleTest");
        requestDto.setAuthor("AuthorTest");
        requestDto.setIsbn("123456789");
        requestDto.setPrice(BigDecimal.valueOf(10.00));
        requestDto.setCategoryIds(List.of(1L));

        Book expectedBook = new Book();
        expectedBook.setId(id);
        expectedBook.setTitle(requestDto.getTitle());
        expectedBook.setAuthor(requestDto.getAuthor());
        expectedBook.setIsbn(requestDto.getIsbn());
        expectedBook.setPrice(requestDto.getPrice());

        BookDto expectedDto = new BookDto();
        expectedDto.setId(expectedBook.getId());
        expectedDto.setTitle(expectedBook.getTitle());

        when(bookRepository.findById(id)).thenReturn(Optional.of(expectedBook));
        when(bookMapper.toBookDto(expectedBook)).thenReturn(expectedDto);
        when(categoryMapperHelper.mapCategoryIdsToCategory(requestDto.getCategoryIds()))
                .thenReturn(Collections.emptySet());

        BookDto actualDto = bookService.update(id, requestDto);
        assertEquals(expectedDto, actualDto);
    }

    @Test
    @DisplayName("""
            
            """)
    public void update_invalidId_notFound() {
        Long id = 1L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("TitleTest");
        requestDto.setAuthor("AuthorTest");
        requestDto.setIsbn("123456789");
        requestDto.setPrice(BigDecimal.valueOf(10.00));
        requestDto.setCategoryIds(List.of(1L));
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> bookService.update(id, requestDto));
        verifyNoInteractions(bookMapper, categoryMapperHelper);

    }
}
