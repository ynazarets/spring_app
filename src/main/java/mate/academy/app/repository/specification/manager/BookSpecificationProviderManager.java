package mate.academy.app.repository.specification.manager;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.app.model.Book;
import mate.academy.app.repository.specification.provider.SpecificationProvider;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager
        implements SpecificationProviderManager<Book> {

    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(()
                        -> new RuntimeException("Can not find specification provider for key: "
                        + key));
    }
}
