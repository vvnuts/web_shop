INSERT INTO categories (category_name)
VALUES ('Техника'),
('Смартфоны'),
('Apple'),
('Сопутствующие товары'),
('Samsung'),
('Наушники'),
('Чехлы'),
('Аудиотехника'),
('Портативные колонки');

INSERT INTO parent_child (parent_id, child_id)
VALUES (1, 2),
(2, 3),
(2, 4),
(2, 5),
(4, 6),
(4, 7),
(1, 8),
(8, 9),
(8, 6);

INSERT INTO characteristics (name, type)
VALUES ('Цвет', 'STRING'),
('Размер аккумулятора', 'INTEGER'),
('Материал', 'STRING'),
('Размер', 'INTEGER'),
('Тип наушников', 'STRING');

INSERT INTO category_characteristic (category_id, characteristic_id)
VALUES (3, 1),
(3, 2),
(3, 3),
(7, 3),
(7, 4),
(6, 1),
(6, 4),
(6, 5);

INSERT INTO items (category_id, item_name, description, quantity, price, sale, mark)
VALUES
   (3, 'iPad Air 4', 'Описание iPad Air 4', 10, 50000, 0, 4),
   (3, 'iPad Air 3', 'Описание iPad Air 3', 10, 40000, 0, 0),
   (3, 'iPad Air 2', 'Описание iPad Air 2', 10, 20000, 0, 0),
   (6, 'Realme GT Neo 3T', 'Описание Realme GT Neo 3T', 10, 5000, 0, 1.5),
   (6, 'Apple iPhone 12', 'Описание Apple iPhone 12', 10, 5000, 0, 0),
   (6, 'Samsung Galaxy S21 FE', 'Описание Samsung Galaxy S21 FE', 10, 5000, 0, 0),
   (6, 'Android Pixel 6A', 'Описание Android Pixel 6A', 10, 5000, 0, 0),
   (7, '3-чехол', 'Описание 3-чехола', 10, 500, 0, 0),
   (7, '3-чехол', 'Описание 3-чехола', 10, 600, 0, 0),
   (7, '3-чехол', 'Описание 3-чехола', 10, 700, 0, 0);

INSERT INTO character_item(num_value, value, characteristic_id, item_id)
VALUES (null, 'BLUE', 1, 1),
(null, 'YELLOW', 1, 2),
(null, 'PURPLE', 1, 3),
(1000, null, 2, 1),
(2000, null, 2, 2),
(3000, null, 2, 3),
(null, 'METAL', 3, 1),
(null, 'METAL', 3, 2),
(null, 'PLASTIC', 3, 3),
(null, 'BLUE', 1, 4),
(null, 'YELLOW', 1, 5),
(null, 'BLACK', 1, 6),
(null, 'BLUE', 1, 7),
(2, null, 4, 4),
(2, null, 4, 5),
(2, null, 4, 6),
(2, null, 4, 7),
(null, 'WIRELESS', 5, 4),
(null, 'WIRELESS', 5, 5),
(null, 'WIRELESS', 5, 6),
(null, 'WIRELESS', 5, 7),
(null, 'METAL', 3, 8),
(null, 'METAL', 3, 9),
(null, 'METAL', 3, 10),
(100, null, 4, 8),
(200, null, 4, 9),
(300, null, 4, 10);

INSERT INTO users (firstname, lastname, email, user_password, role)
VALUES
    ('John', 'Doe', 'johndoe@example.com', '$2a$10$QUoIBFYtMK92NFKkwjdSFexA7l9DNOHwxFM5RHFsx2DE8uCvAUC8W', 'ROLE_ADMIN'),
    ('Jane', 'Smith', 'janesmith@example.com', '$2a$10$a/vDCAy2121lCK1xfDXfWuSV4.4sH7DDj7AMIu1nPj4OwkgUBhdgm', 'ROLE_USER'),
    ('Michael', 'Johnson', 'michaeljohnson@example.com', '$2a$10$wyGIHpBcunI7CvMGIeiKMOwOg8ZM6QTvaFPzUvZcIm39VrLECvhYu', 'ROLE_USER'),
    ('Emily', 'Williams', 'emilywilliams@example.com', '$2a$10$TU3i7bfUQ6tEXSpcgwQojuuvwGMZVN2lFNhsM0Oj9QLZbw9XinOz2', 'ROLE_USER');

INSERT INTO buckets (user_id)
VALUES
(1),
(2),
(3),
(4);

INSERT INTO reviews (mark, review_text, item_id, user_id)
VALUES (5, 'Great product!', 1, 4),
       (4, 'Good value for money.', 1, 2),
       (3, 'Average quality.', 1, 3),
       (2, 'Not as expected.', 4, 4),
       (1, 'Poor quality.', 4, 2);

