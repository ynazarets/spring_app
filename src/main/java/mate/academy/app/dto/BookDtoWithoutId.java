package mate.academy.app.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookDtoWithoutId {
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
}
