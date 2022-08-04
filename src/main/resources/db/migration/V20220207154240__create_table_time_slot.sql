CREATE TABLE time_slot
(
    id          SERIAL PRIMARY KEY,
    day         VARCHAR NOT NULL,
    time_start  VARCHAR,
    time_end    VARCHAR,
    employee_id INTEGER,
    order_id INTEGER,
    FOREIGN KEY (employee_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);