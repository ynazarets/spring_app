package mate.academy.app.mapper;

import mate.academy.app.config.MapperConfig;
import mate.academy.app.dto.BookDto;
import mate.academy.app.dto.CreateBookRequestDto;
import mate.academy.app.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    Book toModel(CreateBookRequestDto requestDto);

    BookDto toBookDto(Book book);
}
