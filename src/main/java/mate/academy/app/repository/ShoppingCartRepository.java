package mate.academy.app.repository;

import java.util.Optional;
import mate.academy.app.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    Optional<ShoppingCart> findByUserId(Long userId);

    @Query("SELECT sc FROM ShoppingCart sc "
            + "LEFT JOIN FETCH sc.cartItems ci "
            + "LEFT JOIN FETCH ci.book b "
            + "WHERE sc.user.id = :userId")
    Optional<ShoppingCart> findByUserIdWithBooksAndCartItems(@Param("userId") Long userId);
}
