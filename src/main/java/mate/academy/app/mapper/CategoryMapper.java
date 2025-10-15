package mate.academy.app.mapper;

import mate.academy.app.dto.category.CategoryDto;
import mate.academy.app.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {

    CategoryDto toCategoryDto(Category category);

    Category toCategory(CategoryDto categoryDto);

    Category updateCategoryFromDto(CategoryDto requestDto, @MappingTarget Category category);

}
