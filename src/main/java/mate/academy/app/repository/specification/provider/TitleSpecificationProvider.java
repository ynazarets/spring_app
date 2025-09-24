package mate.academy.app.repository.specification.provider;

import java.util.Arrays;
import mate.academy.app.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public String getKey() {
        return "title";
    }

    @Override
    public Specification<Book> getSpecification(String[] param) {
        return (root, query, criteriaBuilder) -> root.get("title")
                .in(Arrays.stream(param).toArray());
    }
}
