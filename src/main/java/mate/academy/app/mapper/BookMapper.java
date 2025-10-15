package mate.academy.app.mapper;

import mate.academy.app.config.MapperConfig;
import mate.academy.app.dto.book.BookDto;
import mate.academy.app.dto.book.CreateBookRequestDto;
import mate.academy.app.model.Book;
import mate.academy.app.model.Category;
import mate.academy.app.repository.CategoryRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;

@Mapper(config = MapperConfig.class)
public interface BookMapper {



    Book toModel(CreateBookRequestDto requestDto);

    BookDto toBookDto(Book book);

    void updateBookFromDto(CreateBookRequestDto requestDto, @MappingTarget Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            List<Long> categoriesId = book.getCategories().stream()
                    .map(Category::getId)
                    .toList();
            bookDto.setCategoriesId(categoriesId);
        }
    }
    @AfterMapping
    default void setCategories(@MappingTarget Book book, CreateBookRequestDto requestDto) {
        if (requestDto.getCategoryIds() != null) {
            Set<Category> categories = CategoryRepository
        }
    }
}
