CREATE TABLE profile
(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    sex       VARCHAR      NOT NULL,
    FOREIGN KEY (id) REFERENCES users (id) ON DELETE CASCADE
);