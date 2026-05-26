CREATE TABLE venues
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    address     TEXT,
    city        VARCHAR(100),
    country     VARCHAR(100),
    latitude    DECIMAL(10, 7),
    longitude   DECIMAL(10, 7),
    capacity    INT,

    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255),
    deleted_at  TIMESTAMP,

    version     BIGINT       NOT NULL DEFAULT 0
);
