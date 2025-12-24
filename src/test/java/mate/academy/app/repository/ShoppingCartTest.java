package mate.academy.app.repository;

import jakarta.persistence.EntityManager;
import mate.academy.app.model.Book;
import mate.academy.app.model.CartItem;
import mate.academy.app.model.Category;
import mate.academy.app.model.ShoppingCart;
import mate.academy.app.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    @DisplayName("""
            Find by user ID: should return shopping cart for existing user
            """)
    public void findByUserId_validId_ShouldReturnShoppingCart() {
        User user = new User();
        user.setEmail("email123456");
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setPassword("password");
        userRepository.save(user);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);

        entityManager.flush();
        entityManager.clear();

        Optional<ShoppingCart> actualShoppingCart = shoppingCartRepository.findByUserId(user.getId());

        assertTrue(actualShoppingCart.isPresent());
        assertEquals(actualShoppingCart.get().getId(), shoppingCart.getId());
        assertEquals(user.getId(), actualShoppingCart.get().getUser().getId());
    }

        @Test
        @DisplayName("""
                Find shopping cart by user ID: should fetch cart with all items and books initialized
                """)
        public void findByUserIdWithBooksAndCartItems_validId_ShouldReturnShoppingCart() {
            User user = new User();
            user.setEmail("email123456");
            user.setFirstName("FirstName");
            user.setLastName("LastName");
            user.setPassword("password");
            userRepository.save(user);

            Category category = new Category();
            category.setName("category");
            entityManager.persist(category);

            Book book = new Book();
            book.setPrice(BigDecimal.TEN);
            book.setTitle("title");
            book.setAuthor("author");
            book.getCategories().add(category);
            book.setIsbn("isbn");
            entityManager.persist(book);

            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            shoppingCartRepository.save(shoppingCart);

            CartItem cartItem = new CartItem();
            cartItem.setBook(book);
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setQuantity(2);
            cartItemRepository.save(cartItem);

            entityManager.flush();
            entityManager.clear();

            Optional<ShoppingCart> actualShoppingCart = shoppingCartRepository
                    .findByUserIdWithBooksAndCartItems(user.getId());

            assertTrue(actualShoppingCart.isPresent());

            ShoppingCart cart =  actualShoppingCart.get();

            assertEquals(1, cart.getCartItems().size());

            CartItem actualItem = cart.getCartItems().iterator().next();
            assertEquals(2, actualItem.getQuantity());
            assertEquals(actualItem.getBook().getTitle(), cartItem.getBook().getTitle());
        }
}
