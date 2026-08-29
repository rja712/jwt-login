CREATE TABLE roles
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,

    CONSTRAINT uk_roles_name UNIQUE (name)
);