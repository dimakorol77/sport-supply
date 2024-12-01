-- Заполнение таблицы users
INSERT INTO users (email, password, name, phone_number, role, created_at, updated_at)
VALUES ('user1@example.com', 'password1', 'User One', '1234567890', 'USER', NOW(), NOW()),
       ('user2@example.com', 'password2', 'User Two', '0987654321', 'ADMIN', NOW(), NOW());

-- Заполнение таблицы brands
INSERT INTO brands (name, description, created_at, updated_at)
VALUES ('Brand A', 'Description for Brand A', NOW(), NOW()),
       ('Brand B', 'Description for Brand B', NOW(), NOW());

-- Заполнение таблицы categories
INSERT INTO categories (name, description, parent_category_id, created_at, updated_at)
VALUES ('Category 1', 'Description for Category 1', NULL, NOW(), NOW()),
       ('Category 2', 'Description for Category 2', NULL, NOW(), NOW()),
       ('Subcategory 1', 'Description for Subcategory 1', 1, NOW(), NOW());

-- Заполнение таблицы products
INSERT INTO products (name, description, price, category_id, brand_id, protein_type, vitamin_group, form, created_at,
                      updated_at)
VALUES ('Product 1', 'Description for Product 1', 10.99, 1, 1, 'WHEY', 'Vitamin C', 'POWDER', NOW(), NOW()),
       ('Product 2', 'Description for Product 2', 15.49, 2, 2, 'CASEIN', 'Vitamin B', 'POWDER', NOW(), NOW());

-- Заполнение таблицы carts
INSERT INTO carts (user_id, created_at, updated_at, total_price)
VALUES
    (1, NOW(), NOW(), 25.99),
    (2, NOW(), NOW(), 15.49);

-- Заполнение таблицы cart_items
INSERT INTO cart_items (cart_id, product_id, quantity, price, discount_price, deleted)
VALUES (1, 1, 2, 21.98, 19.99, FALSE),
       (2, 2, 1, 15.49, NULL, FALSE);

-- Заполнение таблицы discounts
INSERT INTO discounts (product_id, discount_price, start_date, end_date, created_at, updated_at)
VALUES (1, 9.99, NOW(), DATE_ADD(NOW(), INTERVAL 10 DAY), NOW(), NOW()),
       (2, 12.99, NOW(), DATE_ADD(NOW(), INTERVAL 5 DAY), NOW(), NOW());

-- Заполнение таблицы favorites
INSERT INTO favorites (user_id, product_id, added_at)
VALUES (1, 1, NOW()),
       (2, 2, NOW());

-- Заполнение таблицы images
INSERT INTO images (url, alt_text, created_at, updated_at, product_id)
VALUES ('https://example.com/image1.jpg', 'Image 1 Description', NOW(), NOW(), 1),
       ('https://example.com/image2.jpg', 'Image 2 Description', NOW(), NOW(), 2);

-- Заполнение таблицы orders
INSERT INTO orders (total_amount, status, delivery_method, delivery_address, contact_info, created_at, updated_at,
                    user_id)
VALUES (19.99, 'PROCESSING', 'COURIER', '123 Main St', 'user1@example.com', NOW(), NOW(), 1),
       (25.99, 'DELIVERED', 'PICKUP', '456 Elm St', 'user2@example.com', NOW(), NOW(), 2);

-- Заполнение таблицы order_items
INSERT INTO order_items (product_id, product_name, product_description, product_category_name, quantity, price,
                         order_id)
VALUES (1, 'Product 1', 'Description for Product 1', 'Category 1', 2, 19.99, 1),
       (2, 'Product 2', 'Description for Product 2', 'Category 2', 1, 25.99, 2);

-- Заполнение таблицы order_status_histories
INSERT INTO order_status_histories (order_id, status, changed_at)
VALUES (1, 'PROCESSING', NOW()),
       (2, 'DELIVERED', NOW());

-- Заполнение таблицы payments
INSERT INTO payments (order_id, amount, status, created_at, updated_at)
VALUES (1, 19.99, 'COMPLETED', NOW(), NOW()),
       (2, 25.99, 'COMPLETED', NOW(), NOW());

-- Заполнение таблицы reviews
INSERT INTO reviews (user_id, product_id, rating, user_comment, created_at, updated_at)
VALUES (1, 1, 5, 'Great product!', NOW(), NOW()),
       (2, 2, 4, 'Good quality', NOW(), NOW());

-- Заполнение таблицы promotions
INSERT INTO promotions (name, description, start_date, end_date, created_at, updated_at)
VALUES ('Summer Sale', 'Discounts on all products', NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), NOW(), NOW()),
       ('Winter Sale', 'Special discounts for winter', NOW(), DATE_ADD(NOW(), INTERVAL 60 DAY), NOW(), NOW());

-- Заполнение таблицы product_promotions
INSERT INTO product_promotions (product_id, promotion_id)
VALUES (1, 1),
       (2, 2);