package mate.academy.app.mapper;

import mate.academy.app.dto.BookDto;
import mate.academy.app.dto.BookDtoWithoutId;
import mate.academy.app.dto.CreateBookRequestDto;
import mate.academy.app.model.Book;

public interface BookMapper {

    BookDtoWithoutId toBookDtoWithoutId(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    BookDto toBookDto(Book book);
}
