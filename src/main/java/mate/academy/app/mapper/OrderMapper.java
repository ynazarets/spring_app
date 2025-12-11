package mate.academy.app.mapper;

import java.util.List;
import mate.academy.app.config.MapperConfig;
import mate.academy.app.dto.order.OrderResponseDto;
import mate.academy.app.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "status", source = "orderStatus")
    OrderResponseDto toDto(Order order);

    List<OrderResponseDto> toDtoList(List<Order> orders);
}
