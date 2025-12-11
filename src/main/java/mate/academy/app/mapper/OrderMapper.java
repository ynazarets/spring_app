package mate.academy.app.mapper;

import java.util.List;
import mate.academy.app.config.MapperConfig;
import mate.academy.app.dto.order.OrderResponseDto;
import mate.academy.app.model.Order;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {

    OrderResponseDto toDto(Order order);

    List<OrderResponseDto> toDtoList(List<Order> orders);
}
