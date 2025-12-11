package mate.academy.app.service;

import mate.academy.app.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.app.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.app.dto.shoppingcart.ShoppingCartDto;
import mate.academy.app.model.User;

public interface ShoppingCartService {

    void createShoppingCart(User user);

    ShoppingCartDto getShoppingCart(Long userId);

    ShoppingCartDto addBookToCart(Long userId, CreateCartItemRequestDto requestDto);

    ShoppingCartDto updateCartItemQuantity(Long userId,
                                                Long cartItemId,
                                                UpdateCartItemRequestDto requestDto);

    void deleteCartItem(Long userId, Long cartItemId);

    void clearShoppingCart(Long userId);
}
