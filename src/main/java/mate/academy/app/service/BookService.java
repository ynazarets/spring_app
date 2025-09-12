package mate.academy.app.service;

import mate.academy.app.model.Book;

import java.util.List;

public interface BookService {

    Book save(Book book);

    List<Book> findAll();
}
