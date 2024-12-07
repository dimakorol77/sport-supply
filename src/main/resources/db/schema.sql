-- 1. users
CREATE TABLE users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  name VARCHAR(255),
  phone_number VARCHAR(255),
  role VARCHAR(50),
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_users_email (email)
);

-- 2. brands
CREATE TABLE brands (
id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  created_at DATETIME,
  updated_at DATETIME,
  PRIMARY KEY (id),
  UNIQUE KEY uq_brands_name (name)
);

-- 3. categories
CREATE TABLE categories (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  parent_category_id BIGINT,
  created_at DATETIME,
  updated_at DATETIME,
  PRIMARY KEY (id),
  UNIQUE KEY uq_categories_name (name),
  FOREIGN KEY (parent_category_id) REFERENCES categories (id)
);

-- 4. products
CREATE TABLE products (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  price DECIMAL(10,2),
  category_id BIGINT,
  brand_id BIGINT,
  protein_type VARCHAR(50),
  vitamin_group VARCHAR(255),
  form VARCHAR(50),
  created_at DATETIME,
  updated_at DATETIME,
  PRIMARY KEY (id),
  FOREIGN KEY (category_id) REFERENCES categories(id),
  FOREIGN KEY (brand_id) REFERENCES brands(id)
);

-- 5. images
CREATE TABLE images (
  id BIGINT NOT NULL AUTO_INCREMENT,
  url VARCHAR(255) NOT NULL,
  alt_text VARCHAR(255),
  created_at DATETIME NOT NULL,
  updated_at DATETIME,
  product_id BIGINT,
  PRIMARY KEY (id),
  FOREIGN KEY (product_id) REFERENCES products(id)
);

-- 6. discounts (ссылается на products)
CREATE TABLE discounts (
  id BIGINT NOT NULL AUTO_INCREMENT,
  product_id BIGINT NOT NULL,
  discount_price DECIMAL(10,2),
  start_date DATETIME,
  end_date DATETIME,
  created_at DATETIME,
  updated_at DATETIME,
  PRIMARY KEY (id),
  FOREIGN KEY (product_id) REFERENCES products(id)
);

-- 7. promotions
CREATE TABLE promotions (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL,
  description VARCHAR(1000),
  start_date DATETIME NOT NULL,
  end_date DATETIME NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  PRIMARY KEY (id)
);

-- 8. product_promotions
CREATE TABLE product_promotions (
  id BIGINT NOT NULL AUTO_INCREMENT,
  product_id BIGINT NOT NULL,
  promotion_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (product_id) REFERENCES products(id),
  FOREIGN KEY (promotion_id) REFERENCES promotions(id)
);

-- 9. carts
CREATE TABLE carts (
  id BIGINT NOT NULL AUTO_INCREMENT,
  created_at DATETIME NOT NULL,
  updated_at DATETIME,
  user_id BIGINT UNIQUE,
  total_price DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 10. cart_items
CREATE TABLE cart_items (
  id BIGINT NOT NULL AUTO_INCREMENT,
  cart_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INT,
  price DECIMAL(10,2),
  discount_price DECIMAL(10,2),
  deleted TINYINT(1),
  PRIMARY KEY (id),
  FOREIGN KEY (cart_id) REFERENCES carts(id),
  FOREIGN KEY (product_id) REFERENCES products(id)
);

-- 11. orders
CREATE TABLE orders (
  id BIGINT NOT NULL AUTO_INCREMENT,
  total_amount DECIMAL(10,2) NOT NULL,
  status VARCHAR(50) NOT NULL,
  delivery_method VARCHAR(50) NOT NULL,
  delivery_address VARCHAR(255),
  contact_info VARCHAR(255),
  created_at DATETIME NOT NULL,
  updated_at DATETIME,
  user_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 12. order_items
CREATE TABLE order_items (
  id BIGINT NOT NULL AUTO_INCREMENT,
  product_id BIGINT,
  product_name VARCHAR(255),
  product_description VARCHAR(255),
  product_category_name VARCHAR(255),
  quantity INT NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  order_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- 13. order_status_histories
CREATE TABLE order_status_histories (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  status VARCHAR(50) NOT NULL,
  changed_at DATETIME NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- 14. payments
CREATE TABLE payments (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  amount DECIMAL(10,2),
  status VARCHAR(50),
  created_at DATETIME,
  updated_at DATETIME,
  PRIMARY KEY (id),
  FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- 15. reviews
CREATE TABLE reviews (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  rating INT,
  user_comment VARCHAR(255),
  created_at DATETIME,
  updated_at DATETIME,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (product_id) REFERENCES products(id)
);

-- 16. favorites
CREATE TABLE favorites (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  added_at DATETIME NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (product_id) REFERENCES products(id)
);