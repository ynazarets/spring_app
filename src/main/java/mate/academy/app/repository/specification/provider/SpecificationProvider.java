package mate.academy.app.repository.specification.provider;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {

    String getKey();

    Specification<T> getSpecification(String[] param);
}
