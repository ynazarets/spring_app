package mate.academy.app.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.app.config.MapperConfig;
import mate.academy.app.dto.cartitem.CartItemDto;
import mate.academy.app.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.app.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shoppingCart", ignore = true)
    @Mapping(target = "book", ignore = true)
    CartItem toModel(CreateCartItemRequestDto requestDto);

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto (CartItem cartItem);

    @Named("getCartItemsResponseDto")
    default Set<CartItemDto> getResponseDto(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }
}