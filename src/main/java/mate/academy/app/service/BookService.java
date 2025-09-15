package mate.academy.app.service;

import java.util.List;
import mate.academy.app.model.Book;

public interface BookService {

    Book save(Book book);

    List<Book> findAll();
}
