CREATE TABLE users
(
    user_id    UUID         NOT NULL,
    username   VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    email      VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);