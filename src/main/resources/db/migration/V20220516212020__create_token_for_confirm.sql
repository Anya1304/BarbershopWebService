CREATE TABLE confirmation_token
(
    id           SERIAL PRIMARY KEY,
    token        VARCHAR(255),
    created_date VARCHAR,
    is_confirm   BOOLEAN NOT NULL,
    user_id      INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);