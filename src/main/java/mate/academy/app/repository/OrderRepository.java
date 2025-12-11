package mate.academy.app.repository;

import mate.academy.app.model.Order;
import mate.academy.app.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT o FROM Order o "
            + "LEFT JOIN FETCH o.orderItems oi "
            + "LEFT JOIN FETCH oi.book b "
            + "WHERE o.user.id = :userId",
            countQuery = "SELECT count(o) FROM Order o WHERE o.user.id = :userId")
    Page<Order> findAllByUserIdWithItemsAndBooks(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT oi FROM OrderItem oi "
            + "LEFT JOIN FETCH oi.book b "
            + "WHERE oi.order.id = :orderId AND oi.order.user.id = :userId")
    Page<OrderItem> findAllByOrderIdAndUserId(@Param("orderId") Long orderId,
                                              @Param("userId") Long userId,
                                              Pageable pageable);

}
