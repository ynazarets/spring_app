package mate.academy.app.repository.specification.provider;

import java.util.Arrays;
import mate.academy.app.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    private static final String KEY = "isbn";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] param) {
        return (root, query, criteriaBuilder) -> root.get(KEY)
                .in(Arrays.stream(param).toArray());
    }
}
