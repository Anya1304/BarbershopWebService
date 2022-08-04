CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR     NOT NULL,
    email    VARCHAR(50) NOT NULL,
    salon_id INTEGER,
    FOREIGN KEY (salon_id) REFERENCES hairdressing_salon (id) ON DELETE CASCADE
);