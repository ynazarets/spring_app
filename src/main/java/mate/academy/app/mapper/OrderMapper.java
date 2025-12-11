package mate.academy.app.mapper;

import mate.academy.app.config.MapperConfig;
import mate.academy.app.dto.order.OrderResponseDto;
import mate.academy.app.model.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {

    OrderResponseDto toDto(Order order);

    List<OrderResponseDto> toDtoList(List<Order> orders);
}
