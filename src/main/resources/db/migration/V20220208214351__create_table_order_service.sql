CREATE TABLE order_service
(
    order_id   INTEGER NOT NULL,
    service_id INTEGER NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (service_id) REFERENCES service (id)
);