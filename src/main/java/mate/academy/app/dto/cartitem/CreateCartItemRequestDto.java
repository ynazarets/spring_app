package mate.academy.app.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateCartItemRequestDto {
    @NotNull
    private Long bookId;
    @Min(1)
    private int quantity;
}
