CREATE TABLE schedule
(
    id                    SERIAL PRIMARY KEY,
    day                   VARCHAR NOT NULL,
    time_from             VARCHAR,
    time_to               VARCHAR,
    hairdressing_salon_id INTEGER NOT NULL,
    FOREIGN KEY (hairdressing_salon_id) REFERENCES hairdressing_salon (id) ON DELETE CASCADE
);