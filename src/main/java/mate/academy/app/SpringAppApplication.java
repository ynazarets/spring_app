package mate.academy.app;

import mate.academy.app.controller.BookController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringAppApplication {
    @Autowired
    private BookController bookController;

    public static void main(String[] args) {
        SpringApplication.run(SpringAppApplication.class, args);
    }
}
