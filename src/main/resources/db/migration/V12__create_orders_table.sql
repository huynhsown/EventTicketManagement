CREATE TABLE orders
(
    id                  UUID PRIMARY KEY,
    reservation_id      UUID REFERENCES reservations(id),
    user_id             BIGINT NOT NULL REFERENCES users(id),
    status              VARCHAR(30) NOT NULL,
    total_amount        NUMERIC(12,2) NOT NULL,

    created_at          TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP NOT NULL,
    created_by          VARCHAR(255),
    updated_by          VARCHAR(255),
    deleted_at          TIMESTAMP,

    version             BIGINT NOT NULL DEFAULT 0
);
