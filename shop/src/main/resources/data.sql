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

INSERT INTO characteristics (name)
VALUES ('Цвет'),
('Размер аккумулятора'),
('Материал'),
('Размер'),
('Тип наушников');

INSERT INTO category_characteristic (category_id, characteristic_id)
VALUES (3, 1),
(3, 2),
(3, 3),
(7, 3),
(7, 4),
(6, 1),
(6, 4),
(6, 5);

INSERT INTO items (category_id, item_name, description, quantity, price, sale)
VALUES
   (3, 'iPad Air 4', 'Описание iPad Air 4', 10, 5000, 0),
   (3, 'iPad Air 3', 'Описание iPad Air 3', 10, 5000, 0),
   (3, 'iPad Air 2', 'Описание iPad Air 2', 10, 5000, 0),
   (6, 'Realme GT Neo 3T', 'Описание Realme GT Neo 3T', 10, 5000, 0),
   (6, 'Apple iPhone 12', 'Описание Apple iPhone 12', 10, 5000, 0),
   (6, 'Samsung Galaxy S21 FE', 'Описание Samsung Galaxy S21 FE', 10, 5000, 0),
   (6, 'Android Pixel 6A', 'Описание Android Pixel 6A', 10, 5000, 0),
   (7, '3-чехол', 'Описание 3-чехола', 10, 5000, 0),
   (7, '3-чехол', 'Описание 3-чехола', 10, 5000, 0),
   (7, '3-чехол', 'Описание 3-чехола', 10, 5000, 0);

INSERT INTO character_item(num_value, characteristic_id, item_id)
VALUES (1, 1, 1),
(1, 1, 2),
(1, 1, 3),
(2, 2, 1),
(2, 2, 2),
(2, 2, 3),
(3, 3, 1),
(3, 3, 2),
(3, 3, 3),
(1, 1, 4),
(1, 1, 5),
(1, 1, 6),
(1, 1, 7),
(2, 4, 4),
(2, 4, 5),
(2, 4, 6),
(2, 4, 7),
(3, 5, 4),
(3, 5, 5),
(3, 5, 6),
(3, 5, 7),
(3, 3, 8),
(3, 3, 9),
(3, 3, 10),
(4, 4, 8),
(4, 4, 9),
(4, 4, 10);
