package mate.academy.app.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import mate.academy.app.model.enums.OrderStatus;

@Getter
@Setter
public class UpdateOrderStatusRequestDto {

    @NotNull(message = "Order status cannot be empty.")
    private OrderStatus status;
}
