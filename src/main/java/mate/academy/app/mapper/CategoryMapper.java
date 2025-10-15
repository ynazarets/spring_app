package mate.academy.app.mapper;

import mate.academy.app.dto.book.BookDto;
import mate.academy.app.dto.category.CategoryDto;
import mate.academy.app.model.Book;
import mate.academy.app.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

public interface CategoryMapper {

    CategoryDto toCategoryDto(Category category);

    Category toCategory(CategoryDto categoryDto);

}
