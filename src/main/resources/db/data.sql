-- Brands
INSERT INTO brands (name, description, created_at, updated_at)
VALUES ('Brand A', 'Description for Brand A', NOW(), NOW()),
       ('Brand B', 'Description for Brand B', NOW(), NOW());

-- Categories
INSERT INTO categories (name, description, created_at, updated_at)
VALUES ('Category 1', 'Description for Category 1', NOW(), NOW()),
       ('Category 2', 'Description for Category 2', NOW(), NOW());

-- Users
INSERT INTO users (email, password, name, phone_number, role, created_at, updated_at)
VALUES
    ('user1@example.com', 'password123', 'User One', '1234567890', 'USER', NOW(), NOW()),
    ('admin@example.com', 'password456', 'Admin User', '0987654321', 'ADMIN', NOW(), NOW());

-- Products
INSERT INTO products (name, description, price, category_id, brand_id, protein_type, form, created_at, updated_at)
VALUES ('Protein Powder', 'Whey Protein Powder for athletes', 25.99, 1, 1, 'WHEY', 'POWDER', NOW(), NOW()),
       ('Casein Capsules', 'Casein Protein Capsules for recovery', 19.99, 2, 2, 'CASEIN', 'CAPSULES', NOW(), NOW());

-- Promotions
INSERT INTO promotions (name, description, start_date, end_date, created_at, updated_at)
VALUES ('Winter Sale', 'Discounts on all products', '2024-11-01', '2024-12-31', NOW(), NOW()),
       ('Black Friday', 'Special Black Friday deals', '2024-11-24', '2024-11-27', NOW(), NOW());

-- Product Promotions
INSERT INTO product_promotions (product_id, promotion_id)
VALUES (1, 1),
       (2, 2);

-- Discounts
INSERT INTO discounts (product_id, discount_price, start_date, end_date, created_at, updated_at)
VALUES (1, 20.99, '2024-11-01', '2024-11-30', NOW(), NOW()),
       (2, 15.99, '2024-11-24', '2024-11-27', NOW(), NOW());

-- Carts
INSERT INTO carts (id, created_at, updated_at, total_price)
VALUES
    (1, NOW(), NOW(), 40.00),
    (2, NOW(), NOW(), 100.00);

-- Cart Items
INSERT INTO cart_items (cart_id, product_id, quantity, price, discount_price, deleted)
VALUES (1, 1, 1, 25.99, 20.99, FALSE),
       (1, 2, 2, 19.99, 15.99, FALSE);

-- Orders
INSERT INTO orders (total_amount, status, delivery_method, delivery_address, contact_info, created_at, updated_at,
                    user_id)
VALUES (50.00, 'CREATED', 'COURIER', '123 Main St', '1234567890', NOW(), NOW(), 1);

-- Order Items
INSERT INTO order_items (order_id, product_id, product_name, product_description, product_category_name, quantity,
                         price)
VALUES (1, 1, 'Protein Powder', 'Whey Protein Powder for athletes', 'Category 1', 1, 25.99),
       (1, 2, 'Casein Capsules', 'Casein Protein Capsules for recovery', 'Category 2', 2, 19.99);

-- Order Status Histories
INSERT INTO order_status_histories (order_id, status, changed_at)
VALUES (1, 'CREATED', NOW()),
       (1, 'WAITING_PAYMENT', NOW());

-- Payments
INSERT INTO payments (order_id, amount, status, created_at, updated_at)
VALUES (1, 50.00, 'PENDING', NOW(), NOW());

-- Reviews
INSERT INTO reviews (user_id, product_id, rating, user_comment, created_at, updated_at)
VALUES (1, 1, 5, 'Great quality protein!', NOW(), NOW()),
       (1, 2, 4, 'Good capsules but a bit expensive.', NOW(), NOW());

-- Favorites
INSERT INTO favorites (user_id, product_id, added_at)
VALUES (1, 1, NOW()),
       (1, 2, NOW());

-- Images
INSERT INTO images (url, alt_text, created_at, updated_at, product_id)
VALUES ('https://example.com/whey_protein.jpg', 'Whey Protein Image', NOW(), NOW(), 1),
       ('https://example.com/casein_capsules.jpg', 'Casein Capsules Image', NOW(), NOW(), 2);
