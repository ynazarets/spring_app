package mate.academy.app.repository;

import mate.academy.app.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByShoppingCartIdAndBookId(Long shoppingCartId, Long bookId);

    Optional<CartItem> findByIdAndShoppingCartId(Long id, Long shoppingCartId);
}
