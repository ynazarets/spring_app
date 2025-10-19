package mate.academy.app.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mate.academy.app.dto.cartitem.CartItemDto;
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
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    private static final String SHOPPING_CART_NOT_FOUND_ERROR = "Can not find shopping cart for user with id: ";
    private static final String CART_ITEM_NOT_FOUND_ERROR = "Can not find cart item by id: ";
    private static final String BOOK_NOT_FOUND_ERROR = "Can not find book by id: ";

    @Override
    @Transactional
    public ShoppingCartDto createShoppingCart(User user) {
        ShoppingCart shoppingCart = shoppingCartMapper.mapUserToShoppingCart(user);
        ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(savedShoppingCart);
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
    public CartItemDto addBookToCart(Long userId, CreateCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(SHOPPING_CART_NOT_FOUND_ERROR + userId));
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND_ERROR + requestDto.getBookId()));
        return cartItemRepository.findByShoppingCartIdAndBookId(shoppingCart.getId(), requestDto.getBookId())
                .map(cartItem -> {
                    cartItem.setQuantity(cartItem.getQuantity() + requestDto.getQuantity());
                    return cartItemMapper.toDto(cartItemRepository.save(cartItem));
                })
                .orElseGet(() -> {
                    CartItem newCartItem = new CartItem();
                    newCartItem.setShoppingCart(shoppingCart);
                    newCartItem.setBook(book);
                    newCartItem.setQuantity(requestDto.getQuantity());
                    CartItem savedCartItem = cartItemRepository.save(newCartItem);
                    return cartItemMapper.toDto(savedCartItem);
                });
    }

    @Override
    @Transactional
    public CartItemDto updateCartItemQuantity(Long userId,
                                              Long cartItemId,
                                              UpdateCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(SHOPPING_CART_NOT_FOUND_ERROR + userId));
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(userId, cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(CART_ITEM_NOT_FOUND_ERROR + cartItemId));
        cartItem.setQuantity(requestDto.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    @Transactional
    public void deleteCartItem(Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(SHOPPING_CART_NOT_FOUND_ERROR
                        + userId));
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException(CART_ITEM_NOT_FOUND_ERROR + cartItemId));
        cartItemRepository.delete(cartItem);
    }
}
