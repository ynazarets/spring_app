package mate.academy.app.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceOrderRequestDto {
    @NotBlank(message = "Shipping address cannot be empty.")
    private String shippingAddress;
}
