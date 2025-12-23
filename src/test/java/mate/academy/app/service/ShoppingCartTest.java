package mate.academy.app.service;

import mate.academy.app.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.app.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.app.dto.shoppingcart.ShoppingCartDto;
import mate.academy.app.exception.EntityNotFoundException;
import mate.academy.app.mapper.CartItemMapper;
import mate.academy.app.mapper.ShoppingCartMapper;
import mate.academy.app.model.Book;
import mate.academy.app.model.CartItem;
import mate.academy.app.model.ShoppingCart;
import mate.academy.app.model.User;
import mate.academy.app.repository.BookRepository;
import mate.academy.app.repository.CartItemRepository;
import mate.academy.app.repository.ShoppingCartRepository;
import mate.academy.app.service.impl.ShoppingCartImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;


import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartTest {
    @InjectMocks
    private ShoppingCartImpl shoppingCartService;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CartItemMapper cartItemMapper;

    @Test
    @DisplayName("""
            Create shopping cart: should successfully initialize and save a new cart for the provided user
            """)
    public void createShoppingCart_ValidUser_ShouldCreateShoppingCart() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        shoppingCartService.createShoppingCart(user);

        verify(shoppingCartRepository, times(1)).save(argThat(cart
                -> cart.getUser().equals(user) && cart.getUser().getId().equals(user.getId())));
    }

    @Test
    @DisplayName("""
            Get shopping cart: should return correct ShoppingCartDto when valid user ID is provided
            """)
    public void getShoppingCart_validUserId_ShouldReturnShoppingCart() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);

        ShoppingCartDto expectedCart = new ShoppingCartDto();
        expectedCart.setId(shoppingCart.getId());
        expectedCart.setUserId(user.getId());

        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expectedCart);

        ShoppingCartDto actualCart = shoppingCartService.getShoppingCart(user.getId());

        assertNotNull(actualCart);
        assertTrue(EqualsBuilder.reflectionEquals(expectedCart, actualCart));

        verify(shoppingCartRepository, times(1)).findByUserId(user.getId());
        verify(shoppingCartMapper, times(1)).toDto(shoppingCart);
    }

    @Test
    @DisplayName("""
            Update cart item: should update item quantity and return updated shopping cart DTO
            """)
    public void updateCartItemQuantity_ValidCartItem_ShouldUpdateCartItemQuantity() {

        Long userId = 1L;
        Long cartItemId = 1L;

        UpdateCartItemRequestDto requestDto = new UpdateCartItemRequestDto();
        requestDto.setQuantity(5);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(10L);

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setQuantity(1);
        cartItem.setShoppingCart(shoppingCart);

        ShoppingCartDto expectedDto = new ShoppingCartDto();
        expectedDto.setId(shoppingCart.getId());

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(shoppingCart));
        when(cartItemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCart.getId()))
                .thenReturn(Optional.of(cartItem));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expectedDto);

        ShoppingCartDto actualDto = shoppingCartService.updateCartItemQuantity(userId, cartItemId, requestDto);

        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);
        assertEquals(requestDto.getQuantity(), cartItem.getQuantity());

        verify(cartItemRepository, times(1)).save(cartItem);
        verify(shoppingCartMapper, times(1)).toDto(shoppingCart);
    }

        @Test
        @DisplayName("""
                Delete cart item: should successfully find and remove item from the user's shopping cart
                """)
        public void deleteCartItem_ValidCartItem_ShouldDeleteCartItem() {

            Long userId = 1L;
            Long cartItemId = 1L;

            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setId(1L);

            CartItem cartItem = new CartItem();
            cartItem.setId(cartItemId);
            cartItem.setQuantity(1);
            cartItem.setShoppingCart(shoppingCart);

            when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(shoppingCart));
            when(cartItemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())).thenReturn(Optional.of(cartItem));
            shoppingCartService.deleteCartItem(userId, cartItemId);

            verify(shoppingCartRepository, times(1)).findByUserId(userId);
            verify(cartItemRepository, times(1)).findByIdAndShoppingCartId(cartItemId, shoppingCart.getId());
            verify(cartItemRepository, times(1)).delete(cartItem);
        }

    @Test
    @DisplayName("""
                Get shopping cart - Should throw EntityNotFoundException when cart for user ID is not found
                """)
    public void getShoppingCart_InvalidUserId_ShouldReturnShoppingCart() {
        Long userId = 1L;


        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, ()
                -> shoppingCartService.getShoppingCart(userId));
    }

    @Test
    @DisplayName("""
                Add book to cart: should create and save a new cart item when the book is not already in the cart"
                """)
    public void addBookToCart_ItemDoesNotExist_ShouldAddBookToCart() {

        Long userId = 1L;
        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto();
        requestDto.setBookId(2L);
        requestDto.setQuantity(3);

        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);

        Book book = new Book();
        book.setId(2L);

        CartItem newCartItem = new CartItem();
        ShoppingCartDto expectedDto = new ShoppingCartDto();

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(requestDto.getBookId())).thenReturn(Optional.of(book));
        when(cartItemRepository.findByShoppingCartIdAndBookId(cart.getId(), requestDto.getBookId()))
                .thenReturn(Optional.empty());
        when(cartItemMapper.toModel(requestDto)).thenReturn(newCartItem);
        when(cartItemRepository.save(newCartItem)).thenReturn(newCartItem);
        when(shoppingCartMapper.toDto(cart)).thenReturn(expectedDto);

        shoppingCartService.addBookToCart(userId, requestDto);

        assertEquals(cart, newCartItem.getShoppingCart());
        assertEquals(book, newCartItem.getBook());
        assertEquals(3, newCartItem.getQuantity());
        verify(cartItemRepository).save(newCartItem);
        assertTrue(cart.getCartItems().contains(newCartItem));
    }
}
