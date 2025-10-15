package mate.academy.app.service;

import mate.academy.app.dto.category.CategoryDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> findAll();

    CategoryDto getById(Long id);

    CategoryDto save(CategoryDto categoryDto);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);
}
