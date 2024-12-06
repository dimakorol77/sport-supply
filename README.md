
## Online Sports Nutrition Store Backend

## Description
This project is an online platform for selling sports nutrition products, designed to provide a seamless shopping 
experience for fitness enthusiasts. Built with a robust technology stack, it incorporates the following features:

- **User Authentication and Authorization**: Secure registration and login processes using Spring Security and JWT 
for token-based authentication.
- **Product Management**: Users can browse, search, and filter a variety of sports nutrition products. 
Admins can manage inventory, update product details, and handle product images.
- **Order Processing**: Users can add products to their cart, place orders, track their order history, 
and view order status changes.
- **Promotion Management**: Admins can create promotions, associate products with promotions, and manage discounts.
- **Favorites and Reviews**: Users can mark products as favorites and leave reviews with ratings.
- **Database Management**: Utilizes MySQL for data storage.
- **API Documentation**: Comprehensive API documentation using Swagger/OpenAPI.
- **Containerization**: Docker for containerizing the application.

## Used Technology Stack:
- **Programming Language**: Java 21
- **Framework**: Spring Boot 3.3.4
- **Security**: Spring Security, JWT (JSON Web Tokens)
- **ORM**: Spring Data JPA/Hibernate
- **Database**: MySQL
- **Documentation**: Swagger/OpenAPI
- **Containerization**: Docker
- **Testing**: JUnit, Mockito
- **Code Simplification**: Lombok

# Database Structure

### Table: users

| Column       | Type         | Description                                           |
|--------------|--------------|-------------------------------------------------------|
| id           | bigint       | Unique identifier, primary key, not null              |
| email        | varchar(255) | User email, unique, not null                          |
| password     | varchar(255) | User password, not null                               |
| name         | varchar(255) | User name                                             |
| phone_number | varchar(255) | User phone number                                     |
| role         | varchar(255) | User role (enum: USER, ADMIN)                         |
| created_at   | timestamp    | Timestamp of row creation, not null                   |
| updated_at   | timestamp    | Timestamp of last update                               |

### Table: products

| Column          | Type         | Description                                                 |
|-----------------|--------------|-------------------------------------------------------------|
| id              | bigint       | Unique identifier, primary key, not null                    |
| name            | varchar(255) | Product name, not null                                      |
| description     | text         | Product description                                         |
| price           | decimal      | Product price                                               |
| category_id     | bigint       | Foreign key to `categories.id`, not null                    |
| brand_id        | bigint       | Foreign key to `brands.id`, not null                        |
| protein_type    | varchar(255) | Type of protein (enum: WHEY, CASEIN, SOY, etc.)             |
| vitamin_group   | varchar(255) | Vitamin group associated with the product                   |
| form            | varchar(255) | Product form (enum: POWDER, CAPSULE, BAR, etc.)             |
| created_at      | timestamp    | Timestamp of row creation                                   |
| updated_at      | timestamp    | Timestamp of last update                                    |
| is_favorite     | boolean      | Flag indicating if the product is favorited (not persisted)  |

### Table: categories

| Column              | Type         | Description                                                |
|---------------------|--------------|------------------------------------------------------------|
| id                  | bigint       | Unique identifier, primary key, not null                   |
| name                | varchar(255) | Category name, unique, not null                             |
| description         | text         | Category description                                       |
| parent_category_id  | bigint       | Foreign key to `categories.id`, nullable (for subcategories) |
| created_at          | timestamp    | Timestamp of row creation                                  |
| updated_at          | timestamp    | Timestamp of last update                                   |

### Table: brands

| Column      | Type         | Description                                              |
|-------------|--------------|----------------------------------------------------------|
| id          | bigint       | Unique identifier, primary key                           |
| name        | varchar(255) | Name of the brand, unique, not null                      |
| description | text         | Description of the brand (optional)                      |
| created_at  | timestamp    | Timestamp of row creation                                |
| updated_at  | timestamp    | Timestamp of last update                                 |

### Table: orders

| Column           | Type         | Description                                                         |
|------------------|--------------|---------------------------------------------------------------------|
| id               | bigint       | Unique identifier, primary key, not null                            |
| total_amount     | decimal      | Total amount of the order, not null                                 |
| status           | varchar(255) | Order status (enum: CREATED, PROCESSING, COMPLETED, CANCELED)       |
| delivery_method  | varchar(255) | Delivery method (enum: COURIER, PICKUP)                             |
| delivery_address | varchar(255) | Address for delivery                                                |
| contact_info     | varchar(255) | Contact information                                                 |
| created_at       | timestamp    | Timestamp of row creation                                           |
| updated_at       | timestamp    | Timestamp of last update                                            |
| user_id          | bigint       | Foreign key to `users.id`, not null                                 |
| payment_id       | bigint       | Foreign key to `payments.id`, nullable                              |

### Table: order_items

| Column                | Type         | Description                                                  |
|-----------------------|--------------|--------------------------------------------------------------|
| id                    | bigint       | Unique identifier, primary key, not null                     |
| quantity              | integer      | Quantity of the product in the order                         |
| price                 | decimal      | Price of the product at the time of order                    |
| discount_price        | decimal      | Discount price of the product (if applicable)                |
| order_id              | bigint       | Foreign key to `orders.id`, not null                          |
| product_id            | bigint       | Foreign key to `products.id`, not null                        |
| product_name          | varchar(255) | Name of the product at the time of order                      |
| product_description   | text         | Description of the product at the time of order               |
| product_category_name | varchar(255) | Category name of the product at the time of order             |

### Table: order_status_histories

| Column     | Type         | Description                                              |
|------------|--------------|----------------------------------------------------------|
| id         | bigint       | Unique identifier, primary key, not null                 |
| order_id   | bigint       | Foreign key to `orders.id`, not null                      |
| status     | varchar(255) | Order status (enum: CREATED, PROCESSING, COMPLETED, CANCELED) |
| changed_at | timestamp    | Timestamp when the status was changed                    |

### Table: payments

| Column      | Type         | Description                                              |
|-------------|--------------|----------------------------------------------------------|
| id          | bigint       | Unique identifier, primary key, not null                 |
| order_id    | bigint       | Foreign key to `orders.id`, not null                      |
| amount      | decimal      | Payment amount                                           |
| status      | varchar(255) | Payment status (enum: PENDING, COMPLETED, FAILED)        |
| created_at  | timestamp    | Timestamp of row creation                                |
| updated_at  | timestamp    | Timestamp of last update                                 |

### Table: promotions

| Column      | Type         | Description                                              |
|-------------|--------------|----------------------------------------------------------|
| id          | bigint       | Unique identifier, primary key, not null                 |
| name        | varchar(255) | Promotion name, not null                                 |
| description | text         | Promotion description                                    |
| start_date  | timestamp    | Promotion start date                                     |
| end_date    | timestamp    | Promotion end date                                       |
| created_at  | timestamp    | Timestamp of row creation                                |
| updated_at  | timestamp    | Timestamp of last update                                 |

### Table: product_promotions

| Column        | Type         | Description                                              |
|---------------|--------------|----------------------------------------------------------|
| id            | bigint       | Unique identifier, primary key, not null                 |
| product_id    | bigint       | Foreign key to `products.id`, not null                    |
| promotion_id  | bigint       | Foreign key to `promotions.id`, not null                  |

### Table: discounts

| Column         | Type         | Description                                              |
|----------------|--------------|----------------------------------------------------------|
| id             | bigint       | Unique identifier, primary key, not null                 |
| product_id     | bigint       | Foreign key to `products.id`, not null                    |
| discount_price | decimal      | Price after discount                                     |
| start_date     | timestamp    | Start date of the discount                               |
| end_date       | timestamp    | End date of the discount                                 |
| created_at     | timestamp    | Timestamp of row creation                                |
| updated_at     | timestamp    | Timestamp of last update                                 |

### Table: carts

| Column      | Type         | Description                                              |
|-------------|--------------|----------------------------------------------------------|
| id          | bigint       | Unique identifier (same as `user_id`), primary key      |
| created_at  | timestamp    | Timestamp of row creation, not null                      |
| updated_at  | timestamp    | Timestamp of last update                                 |
| user_id     | bigint       | Foreign key to `users.id`, unique                        |
| total_price | decimal      | Total price of the cart, not null                        |

### Table: cart_items

| Column         | Type         | Description                                              |
|----------------|--------------|----------------------------------------------------------|
| id             | bigint       | Unique identifier, primary key, not null                 |
| cart_id        | bigint       | Foreign key to `carts.id`, not null                      |
| product_id     | bigint       | Foreign key to `products.id`, not null                    |
| quantity       | integer      | Quantity of the product in the cart                       |
| price          | decimal      | Price of the product at the time of adding to cart        |
| discount_price | decimal      | Discount price of the product (if applicable)             |
| deleted        | boolean      | Flag indicating if the cart item is deleted               |

### Table: images

| Column      | Type         | Description                                              |
|-------------|--------------|----------------------------------------------------------|
| id          | bigint       | Unique identifier, primary key, not null                 |
| url         | varchar(255) | URL of the image, not null                                |
| alt_text    | varchar(255) | Alternative text for the image                            |
| product_id  | bigint       | Foreign key to `products.id`, nullable                     |
| created_at  | timestamp    | Timestamp of row creation                                |
| updated_at  | timestamp    | Timestamp of last update                                 |

### Table: reviews

| Column          | Type         | Description                                              |
|-----------------|--------------|----------------------------------------------------------|
| id              | bigint       | Unique identifier, primary key, not null                 |
| user_id         | bigint       | Foreign key to `users.id`, not null                      |
| product_id      | bigint       | Foreign key to `products.id`, not null                    |
| rating          | integer      | Rating given by the user (e.g., 1-5)                      |
| user_comment    | text         | Review comment                                           |
| created_at      | timestamp    | Timestamp of row creation                                |
| updated_at      | timestamp    | Timestamp of last update                                 |

### Table: favorites

| Column      | Type         | Description                                              |
|-------------|--------------|----------------------------------------------------------|
| id          | bigint       | Unique identifier, primary key, not null                 |
| user_id     | bigint       | Foreign key to `users.id`, not null                      |
| product_id  | bigint       | Foreign key to `products.id`, not null                    |
| added_at    | timestamp    | Timestamp when the product was added to favorites         |
