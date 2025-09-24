package mate.academy.app.repository.specification;

import lombok.RequiredArgsConstructor;
import mate.academy.app.dto.BookSearchParametersDto;
import mate.academy.app.model.Book;
import mate.academy.app.repository.specification.manager.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String AUTHOR_KEY = "author";
    private static final String ISBN_KEY = "isbn";
    private static final String TITLE_KEY = "title";

    private final SpecificationProviderManager<Book> bookSpecificationProvider;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.title() != null && searchParameters.title().length > 0) {
            spec = spec.and(bookSpecificationProvider
                    .getSpecificationProvider(TITLE_KEY)
                    .getSpecification(searchParameters.title()));
        }

        if (searchParameters.author() != null && searchParameters.author().length > 0) {
            spec = spec.and(bookSpecificationProvider
                    .getSpecificationProvider(AUTHOR_KEY)
                    .getSpecification(searchParameters.author()));
        }

        if (searchParameters.isbn() != null && searchParameters.isbn().length > 0) {
            spec = spec.and(bookSpecificationProvider
                    .getSpecificationProvider(ISBN_KEY)
                    .getSpecification(searchParameters.isbn()));
        }
        return spec;
    }
}
