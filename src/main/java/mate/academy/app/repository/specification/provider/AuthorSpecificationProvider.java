package mate.academy.app.repository.specification.provider;

import java.util.Arrays;
import mate.academy.app.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    private static final String KEY = "author";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] param) {
        return (root, query, criteriaBuilder) -> root.get("author")
                .in(Arrays.stream(param).toArray());
    }
}
