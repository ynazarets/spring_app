package mate.academy.app.repository;

import java.util.List;
import mate.academy.app.model.Book;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();
}
