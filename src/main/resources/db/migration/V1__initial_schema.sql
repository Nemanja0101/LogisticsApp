CREATE TABLE users
(
    id             UUID PRIMARY KEY,
    first_name     VARCHAR(50)  NOT NULL,
    last_name      VARCHAR(50)  NOT NULL,
    email          VARCHAR(100) NOT NULL,
    role           VARCHAR(20)  NOT NULL,
    password       TEXT         NOT NULL,
    created_at     TIMESTAMP    NOT NULL,
    deactivated_at TIMESTAMP DEFAULT NULL
);

CREATE UNIQUE INDEX users_emails_active_idx
    ON users (email) WHERE deactivated_at IS NULL;