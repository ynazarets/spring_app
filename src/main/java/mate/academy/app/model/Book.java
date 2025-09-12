package mate.academy.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor(force = true)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Column(nullable = false)
    private String title;
    @NonNull
    @Column(nullable = false)
    private String author;
    @NonNull
    @Column(nullable = false, unique = true)
    private String isbn;
    @NonNull
    @Column(nullable = false)
    private BigDecimal price;
    private String description;
    private String coverImage;
}
