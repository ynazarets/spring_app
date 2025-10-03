package mate.academy.app.service;

import java.util.List;
import mate.academy.app.dto.book.BookDto;
import mate.academy.app.dto.book.BookSearchParametersDto;
import mate.academy.app.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

    BookDto save(CreateBookRequestDto requestDto);

    Page<BookDto> findAll(Pageable pageable);

    BookDto getById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto searchParameters);
}
