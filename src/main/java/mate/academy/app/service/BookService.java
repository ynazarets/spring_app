package mate.academy.app.service;

import java.util.List;
import mate.academy.app.dto.BookDto;
import mate.academy.app.dto.BookSearchParametersDto;
import mate.academy.app.dto.CreateBookRequestDto;

public interface BookService {

    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto getById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto searchParameters);
}
