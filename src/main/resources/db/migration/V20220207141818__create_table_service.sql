CREATE TABLE service
(
    id              SERIAL PRIMARY KEY,
    type_of_service VARCHAR NOT NULL,
    price           INTEGER NOT NULL,
    description     VARCHAR
);