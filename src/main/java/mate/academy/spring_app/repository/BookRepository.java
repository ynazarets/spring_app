package mate.academy.spring_app.repository;

import mate.academy.spring_app.model.Book;

import java.util.List;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();
}
