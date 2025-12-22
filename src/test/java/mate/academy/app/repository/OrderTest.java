package mate.academy.app.repository;

import jakarta.persistence.EntityManager;
import mate.academy.app.model.Book;
import mate.academy.app.model.Category;
import mate.academy.app.model.Order;
import mate.academy.app.model.OrderItem;
import mate.academy.app.model.User;
import mate.academy.app.model.enums.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class OrderTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EntityManager entityManager;

    @DisplayName("""
            Профессиональный (функциональный):"Find all orders by user ID: should return
            a paginated list of orders with items and books initialized
            """)
    @Test
    public void findAllByUserIdWithItemsAndBooks_validInput_ShouldReturnOrder() {
        Category category = new Category();
        category.setName("category" + System.currentTimeMillis());
        entityManager.persist(category);

        Book book = new Book();
        book.setIsbn("123456789");
        book.setAuthor("Author");
        book.setTitle("Title");
        book.setPrice(BigDecimal.TEN);
        entityManager.persist(book);

        User user = new User();
        user.setFirstName("FirstName" + System.currentTimeMillis());
        user.setLastName("LastName" + System.currentTimeMillis());
        user.setEmail("Email" + System.currentTimeMillis() + "gmail.com");
        user.setPassword("Password" + System.currentTimeMillis());
        entityManager.persist(user);

        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(BigDecimal.TEN);
        order.setShippingAddress("Address" + System.currentTimeMillis());
        entityManager.persist(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setBook(book);
        orderItem.setOrder(order);
        orderItem.setQuantity(1);
        orderItem.setPrice(BigDecimal.TEN);
        entityManager.persist(orderItem);

        entityManager.flush();
        entityManager.clear();

        Pageable pageable = PageRequest.of(0,10);
        Page<Order> orderPage = orderRepository.findAllByUserIdWithItemsAndBooks(user.getId(),  pageable);

        assertNotNull(orderPage);
        assertEquals(1, orderPage.getContent().size());

        Order foundOrder = orderPage.getContent().get(0);
        assertNotNull(foundOrder);
        assertEquals(user.getId(), foundOrder.getUser().getId());
        assertEquals(user.getFirstName(), foundOrder.getUser().getFirstName());

        OrderItem actualItem = foundOrder.getOrderItems().iterator().next();
        assertNotNull(actualItem);
        assertEquals(book.getId(), actualItem.getBook().getId());
        assertEquals(book.getTitle(), actualItem.getBook().getTitle());
        assertEquals(1, actualItem.getQuantity());
    }

    @Test
    @DisplayName("""
            Find order item by IDs: should return the specific
            item only if it belongs to the correct order and user
            """)
    public void findOrderItemByIdAndOrderIdAndOrderUserId_validInput_ShouldReturnOrderItem() {

        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Author");
        book.setIsbn("ISBN-" + System.currentTimeMillis());
        book.setPrice(BigDecimal.TEN);
        entityManager.persist(book);

        User user = new User();
        user.setEmail("user" + System.currentTimeMillis() + "@test.com");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Doe");
        entityManager.persist(user);

        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress("Address");
        order.setTotal(BigDecimal.TEN);
        entityManager.persist(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(book);
        orderItem.setQuantity(5);
        orderItem.setPrice(BigDecimal.TEN);
        entityManager.persist(orderItem);

        entityManager.flush();
        entityManager.clear();

        Optional<OrderItem> actual = orderRepository.findOrderItemByIdAndOrderIdAndOrderUserId(
                orderItem.getId(),
                order.getId(),
                user.getId()
        );

        assertTrue(actual.isPresent());
        assertEquals(orderItem.getId(), actual.get().getId());
        assertEquals("Test Book", actual.get().getBook().getTitle()); // Проверка JOIN FETCH
        assertEquals(order.getId(), actual.get().getOrder().getId());
    }
}
