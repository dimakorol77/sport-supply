-- Таблица users
INSERT INTO users (id, email, password, name, phone_number, role, created_at, updated_at) VALUES
(1, 'user1@example.com', 'password1', 'User One', '1234567890', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'user2@example.com', 'password2', 'User Two', '0987654321', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'admin@example.com', 'adminpassword', 'Admin User', '1112223333', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Таблица brands
INSERT INTO brands (id, name, description, created_at, updated_at) VALUES
(1, 'BrandOne', 'Description of Brand One', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'BrandTwo', 'Description of Brand Two', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Таблица categories
INSERT INTO categories (id, name, description, created_at, updated_at) VALUES
(1, 'CategoryOne', 'Description of Category One', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'CategoryTwo', 'Description of Category Two', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Таблица products
INSERT INTO products (id, name, description, price, category_id, brand_id, created_at, updated_at) VALUES
(1, 'ProductOne', 'Description of Product One', 19.99, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'ProductTwo', 'Description of Product Two', 29.99, 2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Таблица carts
INSERT INTO carts (id, user_id, created_at, updated_at, total_price) VALUES
(1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0.00),
(2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0.00);

-- Таблица cart_items
INSERT INTO cart_items (id, cart_id, product_id, quantity, price, discount_price, deleted) VALUES
(1, 1, 1, 2, 19.99, 15.99, 0),
(2, 2, 2, 1, 29.99, 24.99, 0);

-- Таблица orders
INSERT INTO orders (id, total_amount, status, delivery_method, delivery_address, contact_info, user_id, created_at, updated_at) VALUES
(1, 49.99, 'CREATED', 'COURIER', '123 Street, City', 'Contact Info 1', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 79.99, 'SHIPPED', 'PICKUP', '456 Avenue, City', 'Contact Info 2', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Таблица order_items
INSERT INTO order_items (id, order_id, product_id, product_name, product_description, product_category_name, quantity, price) VALUES
(1, 1, 1, 'ProductOne', 'Description of Product One', 'CategoryOne', 2, 19.99),
(2, 2, 2, 'ProductTwo', 'Description of Product Two', 'CategoryTwo', 1, 29.99);

-- Таблица reviews
INSERT INTO reviews (id, user_id, product_id, rating, user_comment, created_at, updated_at) VALUES
(1, 1, 1, 5, 'Great product!', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 2, 4, 'Good quality.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Таблица discounts
INSERT INTO discounts (id, product_id, discount_price, start_date, end_date, created_at, updated_at) VALUES
(1, 1, 4.00, CURRENT_TIMESTAMP, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 30 DAY), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 5.00, CURRENT_TIMESTAMP, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 15 DAY), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Таблица promotions
INSERT INTO promotions (id, name, description, start_date, end_date, created_at, updated_at) VALUES
(1, 'Summer Sale', 'Discount 10%', CURRENT_TIMESTAMP, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 60 DAY), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Таблица product_promotions
INSERT INTO product_promotions (id, product_id, promotion_id) VALUES
(1, 1, 1),
(2, 2, 1);

-- Таблица favorites
INSERT INTO favorites (id, user_id, product_id, added_at) VALUES
(1, 1, 1, CURRENT_TIMESTAMP),
(2, 2, 2, CURRENT_TIMESTAMP);

-- Таблица payments
INSERT INTO payments (id, order_id, amount, status, created_at, updated_at) VALUES
(1, 1, 49.99, 'PENDING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 79.99, 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);