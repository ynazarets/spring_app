package mate.academy.app.repository;

import java.util.Optional;
import mate.academy.app.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    Optional<ShoppingCart> findByUserId(Long userId);
}
