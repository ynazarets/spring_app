package mate.academy.app.service;

import java.util.List;
import mate.academy.app.dto.BookDto;
import mate.academy.app.dto.BookDtoWithoutId;
import mate.academy.app.dto.CreateBookRequestDto;

public interface BookService {

    BookDtoWithoutId save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto getById(Long id);
}
