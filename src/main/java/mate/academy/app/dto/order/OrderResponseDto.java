package mate.academy.app.dto.order;

import lombok.Getter;
import lombok.Setter;
import mate.academy.app.model.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private OrderStatus status;
    private BigDecimal total;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private List<OrderItemResponseDto> orderItems;
}
