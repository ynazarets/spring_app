INSERT INTO USERS (id, email, password, first_name, last_name, shipping_address, is_deleted)
VALUES (1, 'John@gmail.com', 'password12345', 'John',
        'Doe', '123 Main Street', false);

INSERT INTO shopping_carts (user_id, is_deleted)
VALUES (1, false);

INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);

INSERT INTO books (id, title, author, isbn, price, is_deleted)
VALUES (1, 'Effective Java', 'Joshua Bloch', '978-0134685991', 45.00, false),
       (2, 'Clean Code', 'SomeAuthor', '978-0132350884', 35.00, false);

INSERT INTO cart_items (shopping_cart_id, book_id, quantity) VALUES (1, 1, 1);