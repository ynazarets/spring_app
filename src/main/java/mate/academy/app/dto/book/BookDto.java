package mate.academy.app.dto.book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import mate.academy.app.dto.category.CategoryDto;
import mate.academy.app.model.Category;

@Setter
@Getter
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
    private List<Long> categoriesId;
}
