package mate.academy.spring_app.service;

import mate.academy.spring_app.model.Book;

import java.util.List;

public interface BookService {

    Book save(Book book);

    List<Book> findAll();
}
