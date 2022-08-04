CREATE TABLE orders
(
    id          SERIAL PRIMARY KEY,
    order_date  VARCHAR      NOT NULL,
    description VARCHAR(255) NOT NULL,
    user_id     INTEGER      NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);