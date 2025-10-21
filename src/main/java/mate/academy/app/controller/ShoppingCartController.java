package mate.academy.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.app.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.app.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.app.dto.shoppingcart.ShoppingCartDto;
import mate.academy.app.model.User;
import mate.academy.app.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Shopping Cart",
        description = "Operations for managing the user's shopping cart and cart items"
)
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get shopping cart",
            description = "Get shopping cart by user id")
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        return shoppingCartService.getShoppingCart(userId);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add book to cart",
            description = "Add book to cartItem by user id")
    public ShoppingCartDto addBookToCart(Authentication authentication,
                                 @RequestBody @Valid CreateCartItemRequestDto requestDto
    ) {
        Long userId = getUserIdFromAuthentication(authentication);
        return shoppingCartService.addBookToCart(userId, requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update item quantity",
            description = "Changes the quantity of the specified item in the cart.")
    public ShoppingCartDto updateCartItemQuantity(
            Authentication authentication,
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemRequestDto requestDto
    ) {
        Long userId = getUserIdFromAuthentication(authentication);
        return shoppingCartService.updateCartItemQuantity(userId, cartItemId, requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Remove item from cart",
            description = "Deletes a cart item by its ID.")
    public void deleteCartItem(Authentication authentication, @PathVariable Long cartItemId) {
        Long userId = getUserIdFromAuthentication(authentication);
        shoppingCartService.deleteCartItem(userId, cartItemId);
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return principal.getId();
    }
}
