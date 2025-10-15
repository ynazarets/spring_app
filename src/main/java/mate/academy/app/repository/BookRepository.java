package mate.academy.app.repository;

import mate.academy.app.model.Book;
import mate.academy.app.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>,
        JpaSpecificationExecutor<Book> {

    List<Book> findAllByCategory(Category category);

}
