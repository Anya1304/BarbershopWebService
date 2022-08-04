CREATE TABLE user_role
(
    role_name VARCHAR NOT NULL,
    user_id   INTEGER NOT NULL,
    FOREIGN KEY (role_name) REFERENCES role (name),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);