package mate.academy.app;

import java.math.BigDecimal;

import mate.academy.app.controller.BookController;
import mate.academy.app.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApplicationStart {
    @Autowired
    private BookController bookController;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationStart.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Book 1");
            book.setAuthor("Author 1");
            book.setIsbn("ISBN 1");
            book.setPrice(BigDecimal.valueOf(499.99));

            bookController.save(book);

            System.out.println(bookController.findAll());
        };
    }
}
