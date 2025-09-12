package mate.academy.spring_app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NonNull;
import java.math.BigDecimal;

@Entity
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NonNull
    @Column(nullable = false)
    String title;
    @NonNull
    @Column(nullable = false)
    String author;
    @NonNull
    @Column(nullable = false, unique = true)
    String isbn;
    @NonNull
    @Column(nullable = false)
    BigDecimal price;
    String description;
    String coverImage;

    public Book() {
    }
}
