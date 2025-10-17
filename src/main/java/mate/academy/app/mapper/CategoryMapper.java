package mate.academy.app.mapper;

import mate.academy.app.config.MapperConfig;
import mate.academy.app.dto.category.CategoryDto;
import mate.academy.app.dto.category.CreateCategoryRequestDto;
import mate.academy.app.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {

    CategoryDto toCategoryDto(Category category);

    Category toCategory(CreateCategoryRequestDto categoryDto);

    Category updateCategoryFromDto(CreateCategoryRequestDto requestDto,
                                   @MappingTarget Category category);

}
