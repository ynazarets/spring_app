package mate.academy.app.repository;

import mate.academy.app.model.Book;

import java.util.List;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();
}
