package mate.academy.app.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.app.model.Category;
import mate.academy.app.repository.CategoryRepository;
import org.springframework.stereotype.Component;

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
