CREATE TABLE hairdressing_salon
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    address     VARCHAR(255) NOT NULL
);