package mate.academy.app.repository.impl;

import jakarta.persistence.criteria.CriteriaQuery;
import mate.academy.app.model.Book;
import mate.academy.app.repository.BookRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public BookRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Cannot save book with title: "
                    + book.getTitle() , e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<Book> query = session.getCriteriaBuilder()
                    .createQuery(Book.class);
            return session.createQuery(query).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Cannot find books", e);
        }
    }
}
