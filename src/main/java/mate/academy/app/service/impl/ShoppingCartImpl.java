package mate.academy.app.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
import mate.academy.app.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartImpl implements ShoppingCartService {

    private static final String SHOPPING_CART_NOT_FOUND_ERROR
            = "Can not find shopping cart for user with id: ";
    private static final String CART_ITEM_NOT_FOUND_ERROR
            = "Can not find cart item by id: ";
    private static final String BOOK_NOT_FOUND_ERROR
            = "Can not find book by id: ";

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    @Transactional
    public ShoppingCartDto createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto getShoppingCart(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(SHOPPING_CART_NOT_FOUND_ERROR
                        + userId));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto addBookToCart(Long userId, CreateCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(SHOPPING_CART_NOT_FOUND_ERROR
                        + userId));
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND_ERROR
                        + requestDto.getBookId()));
        return cartItemRepository.findByShoppingCartIdAndBookId(shoppingCart.getId(),
                        requestDto.getBookId())
                .map(cartItem -> {
                    cartItem.setQuantity(cartItem.getQuantity() + requestDto.getQuantity());
                    cartItemRepository.save(cartItem);
                    return shoppingCartMapper.toDto(shoppingCart);
                })
                .orElseGet(() -> {
                    CartItem newCartItem = cartItemMapper.toModel(requestDto);
                    newCartItem.setShoppingCart(shoppingCart);
                    newCartItem.setBook(book);
                    newCartItem.setQuantity(requestDto.getQuantity());
                    CartItem savedCartItem = cartItemRepository.save(newCartItem);
                    shoppingCart.getCartItems().add(savedCartItem);
                    return shoppingCartMapper.toDto(shoppingCart);
                });
    }

    @Override
    @Transactional
    public ShoppingCartDto updateCartItemQuantity(Long userId,
                                              Long cartItemId,
                                              UpdateCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(SHOPPING_CART_NOT_FOUND_ERROR
                        + userId));
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(userId,
                        shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException(CART_ITEM_NOT_FOUND_ERROR
                        + cartItemId));
        cartItem.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public void deleteCartItem(Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(SHOPPING_CART_NOT_FOUND_ERROR
                        + userId));
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId,
                        shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException(CART_ITEM_NOT_FOUND_ERROR
                        + cartItemId));
        cartItemRepository.delete(cartItem);
    }
}
