package mate.academy.app.mapper;

import lombok.RequiredArgsConstructor;
import mate.academy.app.dto.category.CategoryDto;
import mate.academy.app.model.Category;
import mate.academy.app.repository.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class CategoryMapperHelper {
    private final CategoryRepository categoryRepository;

    public Set<Category> mapCategoryIdsToCategory(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(categoryRepository.findAllById(ids));
    }
}
