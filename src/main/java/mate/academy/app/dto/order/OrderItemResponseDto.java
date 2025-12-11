package mate.academy.app.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemResponseDto {
    private Long id;
    private Long bookId;
    private int quantity;
}
