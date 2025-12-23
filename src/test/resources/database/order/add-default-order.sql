
INSERT INTO USERS (id, email, password, first_name, last_name, shipping_address, is_deleted)
VALUES (1, 'John@gmail.com', 'password12345', 'John',
        'Doe', '123 Main Street', false);

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1);

INSERT INTO books (id, title, author, isbn, price, is_deleted)
VALUES (1, 'Effective Java', 'Joshua Bloch', '978-0134685991', 60.00, false),
       (2, 'Clean Code', 'SomeAuthor', '978-0132350884', 40.00, false);

INSERT INTO shopping_carts (user_id, is_deleted)
VALUES (1,  false);

INSERT INTO cart_items (shopping_cart_id, book_id, quantity)
VALUES (1, 2, 1);

INSERT INTO orders (id, user_id, status, order_date, shipping_address, total, is_deleted)
VALUES (100, 1, 'PENDING', now(), '123 Main Street', 60, false);

INSERT INTO order_items (id, order_id, book_id, price, quantity, is_deleted)
VALUES (100, 100, 1, 60.00, 1, false);