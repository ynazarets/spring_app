package mate.academy.app.mapper;

import mate.academy.app.config.MapperConfig;
import mate.academy.app.dto.shoppingcart.ShoppingCartDto;
import mate.academy.app.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItems", source = "cartItems",
            qualifiedByName = "getCartItemsResponseDto")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}