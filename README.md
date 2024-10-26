
## Online Sports Nutrition Store Backend

## Description
This project is an online platform for selling sports nutrition products, designed to provide a seamless shopping experience for fitness enthusiasts. Built with a robust technology stack, it incorporates the following features:

User Authentication and Authorization: Secure registration and login processes using Spring Security and JWT for token-based authentication.
Product Management: Users can browse, search, and filter a variety of sports nutrition products. Admins can manage inventory and update product details.
Order Processing: Users can add products to their cart, place orders, and track their order history.
Payment Integration: Supports payment processing for secure transactions.
Database Management: Utilizes MySQL for data storage and Flyway/Liquibase for database versioning and migration.

## Used Technology Stack:
- Java 11+
- Spring Boot 2.x
- Spring Security
- Spring Data JPA/Hibernate
- MySQL
- Flyway/Liquibase
- JWT (JSON Web Tokens)
- Swagger/OpenAPI
- Docker
- JUnit, Mockito

# Database Structure

## Table: users
| Column        | Type         | Description                                 |
|---------------|--------------|---------------------------------------------|
| id            | bigint       | id key of row - unique, not null, primary key |
| email         | varchar(255) | user email, not null, unique                |
| password      | varchar(255) | user password, not null                     |
| name          | varchar(255) | user name                                   |
| phoneNumber   | varchar(255) | user phone number                           |
| role          | varchar(255) | user role (enum)                           |
| createdAt     | timestamp    | timestamp of row creation, not null        |
| updatedAt     | timestamp    | timestamp of last update                    |


## Table: products
| Column        | Type         | Description                                  |
|---------------|--------------|----------------------------------------------|
| id            | bigint       | id key of row - unique, not null, primary key |
| name          | varchar(255) | product name, not null                       |
| description   | text         | product description                          |
| price         | decimal      | product price                                |
| category_id   | bigint       | id of the category associated with the product |
| brand_id      | bigint       | id of the brand associated with the product  |
| protein_type  | varchar(255) | type of protein (enum)                      |
| vitamin_group  | varchar(255) | vitamin group                                |
| form          | varchar(255) | product form (enum)                         |
| createdAt     | timestamp    | timestamp of row creation                    |
| updatedAt     | timestamp    | timestamp of last update                     |
| isFavorite     | boolean      | flag indicating if the product is favorited (not persisted) |

## Table: categories
| Column        | Type         | Description                                  |
|---------------|--------------|----------------------------------------------|
| id            | bigint       | id key of row - unique, not null, primary key |
| name          | varchar(255) | category name, not null, unique              |
| description   | text         | category description                          |
| parent_category_id | bigint  | id of the parent category                    |
| createdAt     | timestamp    | timestamp of row creation                    |
| updatedAt     | timestamp    | timestamp of last update                     |

## Table: brands
| Column      | Type         | Description                                         |
|-------------|--------------|-----------------------------------------------------|
| id          | bigint       | Unique identifier for the brand (Primary Key)      |
| name        | varchar(255) | Name of the brand (cannot be null, must be unique) |
| description | text         | Description of the brand (optional)                 |
| products    | List<Product>| List of products associated with the brand          |
| createdAt   | timestamp    | Timestamp indicating when the brand was created     |
| updatedAt   | timestamp    | Timestamp indicating when the brand was last updated |


## Table: orders
| Column        | Type         | Description                                  |
|---------------|--------------|----------------------------------------------|
| id            | bigint       | id key of row - unique, not null, primary key |
| total_amount  | decimal      | total amount of the order, not null         |
| status        | varchar(255) | order status (enum)                         |
| delivery_method | varchar(255)| delivery method (enum)                      |
| delivery_address | varchar(255)| address for delivery                       |
| contact_info  | varchar(255) | contact information                          |
| createdAt     | timestamp    | timestamp of row creation                    |
| updatedAt     | timestamp    | timestamp of last update                     |
| user_id       | bigint       | id of the user who made the order, not null |
| payment_id    | bigint       | id of the associated payment                 |

## Table: order_items
| Column        | Type         | Description                                  |
|---------------|--------------|----------------------------------------------|
| id            | bigint       | id key of row - unique, not null, primary key |
| quantity      | integer      | quantity of the product in the order        |
| price         | decimal      | price of the product at the time of order   |
| discount_price | decimal     | discount price of the product (if applicable) |
| order_id      | bigint       | id of the order associated with this item, not null |
| product_id    | bigint       | id of the product that was ordered, not null |

## Table: order_status_histories
| Column        | Type         | Description                                  |
|---------------|--------------|----------------------------------------------|
| id            | bigint       | id key of row - unique, not null, primary key |
| order_id      | bigint       | id of the order associated with the history, not null |
| status        | varchar(255) | order status (enum)                         |
| changedAt     | timestamp    | timestamp when the status was changed       |

## Table: payments
| Column        | Type         | Description                                  |
|---------------|--------------|----------------------------------------------|
| id            | bigint       | id key of row - unique, not null, primary key |
| order_id      | bigint       | id of the order associated with the payment, not null |
| amount        | decimal      | payment amount                               |
| status        | varchar(255) | payment status (enum)                       |
| createdAt     | timestamp    | timestamp of row creation                    |

## Table: promotions
| Column        | Type         | Description                                  |
|---------------|--------------|----------------------------------------------|
| id            | bigint       | id key of row - unique, not null, primary key |
| name          | varchar(255) | promotion name                               |
| description   | text         | promotion description                        |
| startDate     | timestamp    | promotion start date                         |
| endDate       | timestamp    | promotion end date                           |
| createdAt     | timestamp    | timestamp of row creation                    |
| updatedAt     | timestamp    | timestamp of last update                     |

## Table: product_promotions
| Column        | Type         | Description                                  |
|---------------|--------------|----------------------------------------------|
| id            | bigint       | id key of row - unique, not null, primary key |
| product_id    | bigint       | id of the product associated with the promotion, not null |
| promotion_id   | bigint       | id of the promotion associated with the product, not null |

## Table: discounts
| Column        | Type         | Description                                  |
|---------------|--------------|----------------------------------------------|
| id            | bigint       | id key of row - unique, not null, primary key |
| product_id    | bigint       | id of the product associated with the discount, not null |
| discount_price | decimal      | price after discount                         |
| startDate     | timestamp    | start date of the discount                   |
| endDate       | timestamp    | end date of the discount                     |

## Table: carts
| Column        | Type         | Description                                  |
|---------------|--------------|----------------------------------------------|
| id            | bigint       | user_id used as cart ID, unique            |
| createdAt     | timestamp    | timestamp of row creation, not null        |
| updatedAt     | timestamp    | timestamp of last update                    |
| user_id       | bigint       | id of the user who owns the cart            |

## Table: cart_items
| Column        | Type         | Description                                  |
|---------------|--------------|----------------------------------------------|
| id            | bigint       | id key of row - unique, not null, primary key |
| cart_id       | bigint       | id of the cart associated with this item, not null |
| product_id    | bigint       | id of the product associated with this item, not null |
| quantity       | integer      | quantity of the product in the cart         |
| price         | decimal      | price of the product at the time of adding to cart |
| discount_price | decimal      | discount price of the product (if applicable) |


## Table: images
| Column        | Type         | Description                                  |
|---------------|--------------|----------------------------------------------|
| id            | bigint       | id key of row - unique, not null, primary key |
| url           | varchar(255) | URL of the image, not null                  |
| altText       | varchar(255) | alternative text for the image               |
| product_id    | bigint       | id of the product associated with the image  |
| createdAt     | timestamp    | timestamp of row creation                    |
| updatedAt     | timestamp    | timestamp of last update                     |


## Table: reviews
| Column        | Type         | Description                                  |
|---------------|--------------|----------------------------------------------|
| id            | bigint       | id key of row - unique, not null, primary key |
| user_id       | bigint       | id of the user who created the review, not null |
| product_id    | bigint       | id of the product being reviewed, not null  |
| rating        | integer      | rating given by the user                     |
| comment       | text         | review comment                               |
| createdAt     | timestamp    | timestamp of row creation                    |
