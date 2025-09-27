package mate.academy.app.repository.specification.manager;

import mate.academy.app.repository.specification.provider.SpecificationProvider;

public interface SpecificationProviderManager<T> {
    SpecificationProvider<T> getSpecificationProvider(String key);
}
