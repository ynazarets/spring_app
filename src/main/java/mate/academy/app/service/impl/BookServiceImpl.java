package mate.academy.app.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.app.model.Book;
import mate.academy.app.repository.BookRepository;
import mate.academy.app.service.BookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
