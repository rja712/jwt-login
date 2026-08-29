CREATE TABLE permissions
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,

    CONSTRAINT uk_permissions_name UNIQUE (name)
);