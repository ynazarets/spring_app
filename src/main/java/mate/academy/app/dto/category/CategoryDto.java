package mate.academy.app.dto.category;

import lombok.Getter;
import lombok.Setter;
import mate.academy.app.dto.book.BookDto;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
}
